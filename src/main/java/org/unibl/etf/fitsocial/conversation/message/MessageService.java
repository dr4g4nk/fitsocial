package org.unibl.etf.fitsocial.conversation.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import core.util.CurrentUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.auth.fcmtoken.FcmTokenRepository;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.conversation.attachment.Attachment;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentService;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserRepository;
import org.unibl.etf.fitsocial.notification.FcmConfigs;
import org.unibl.etf.fitsocial.notification.FirebaseNotificationService;
import org.unibl.etf.fitsocial.notification.NotifikationData;

import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService extends BaseSoftDeletableServiceImpl<Message, MessageDto, MessageDto.List, MessageDto.Update, MessageDto.Create, Long> {
    protected IMapper<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create> attachmentMapper;
    protected BaseSoftDeletableRepository<Attachment, Long> attachmentRepository;
    protected ChatUserRepository chatUserRepository;
    protected MessageRepository messageRepository;
    protected MessageMapper messageMapper;
    private final UserMapper userMapper;
    private final AttachmentService attachmentService;
    private final FirebaseNotificationService notificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final ObjectMapper objectMapper;

    @Value("base.url")
    private String BASE_URL;

    public MessageService(MessageRepository repository, ChatUserRepository chatUserRepository, BaseSoftDeletableRepository<Attachment, Long> attachmentRepository, MessageMapper mapper, IMapper<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create> attachmentMapper, UserMapper userMapper, AttachmentService attachmentService, FirebaseNotificationService notificationService, FcmTokenRepository fcmTokenRepository, ObjectMapper objectMapper) {
        super(repository, mapper);
        this.messageMapper = mapper;
        this.attachmentMapper = attachmentMapper;
        this.chatUserRepository = chatUserRepository;
        this.attachmentRepository = attachmentRepository;
        this.messageRepository = repository;
        this.userMapper = userMapper;
        this.attachmentService = attachmentService;
        this.notificationService = notificationService;
        this.fcmTokenRepository = fcmTokenRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseDto<MessageDto, Message> save(MessageDto.Create dto) {
        ResponseDto<MessageDto, Message> response;
        try {
            Attachment attachment = null;
            if (dto.attachment() != null) {
                var res = attachmentService.save(dto.attachment());
                if (res.isSuccess()) attachment = res.getEntity();
            }

            Message entity = mapper.fromCreateDto(dto);

            if (attachment != null) entity.setAttachment(attachment);

            var userDetail = getUserDetails();
            var userId = userDetail.orElse(new CurrentUserDetails()).getId();
            var chatUserOpt = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(dto.chatId(), userId);

            if (chatUserOpt.isPresent()) {
                var hasAttachment = dto.attachment() != null;
                var hasVideo = hasAttachment && dto.attachment().contentType().startsWith("video");
                var hasImage = hasAttachment && dto.attachment().contentType().startsWith("image");

                var chatUser = chatUserOpt.get();
                entity.setChatUser(chatUser);

                var isGroup = chatUserRepository.isGroupChat(chatUser.getChat().getId());

                if (hasAttachment) {
                    String label = "Šalje dokument";
                    if (hasVideo) label = "Šalje video";
                    else if (hasImage) label = "Šalje sliku";
                    entity.setLabel(label);
                }

                var message = repository.saveAndFlush(entity);

                entityManager.refresh(message);

                var messageDto = mapper.toDto(message);


                var chatUsers = chatUserRepository.findAllByChatIdInAndNotUserId(Collections.singletonList(dto.chatId()), userId);
                var userIds = chatUsers.stream().map(cu -> cu.getUser().getId()).toList();
                var tokens = fcmTokenRepository.findAllByUserIdInLatest(userIds);

                String subject = chatUser.getChat().getSubject();

                if (subject == null || subject.isBlank()) {
                    if (userIds.size() > 1) {
                        subject = chatUsers.stream().map(cu -> cu.getUser().getFirstName()).collect(Collectors.joining(", "));
                    } else {
                        subject = chatUser.getUser().getFirstName() + " " + chatUser.getUser().getLastName();
                    }
                } else subject = "";

                messageDto = new MessageDto(messageDto.id(), messageDto.chatId(), messageDto.user(), messageDto.content(), messageDto.label(), messageDto.createdAt(), messageDto.updatedAt(), true, messageDto.attachment(), chatUser.getChat().getSubject(), isGroup);
                response = new ResponseDto<MessageDto, Message>(messageDto, message);

                var hasMedia = hasVideo || hasImage;

                MessageDto finalMessageDto = new MessageDto(messageDto.id(), messageDto.chatId(), messageDto.user(), messageDto.content(), messageDto.label(), messageDto.createdAt(), messageDto.updatedAt(), false, messageDto.attachment(), chatUser.getChat().getSubject(), isGroup);
                ;
                String body = hasMedia ? finalMessageDto.label() : finalMessageDto.content();
                var data = new HashMap<String, String>();
                data.put("data", objectMapper.writeValueAsString(finalMessageDto));
                data.put("type", "chat_messages");
                data.put("chatId", finalMessageDto.chatId().toString());
                data.put("title", subject);
                data.put("body", body);

                String finalSubject = subject;
                String imageUrl = finalMessageDto.attachment() == null ? null : BASE_URL + "/api/attachment/" + finalMessageDto.attachment().id() + "/stream"
                        + (finalMessageDto.attachment().contentType().startsWith("video") ? "?thumbnail=true" : "");


                var notificationData = new NotifikationData(null, null, null, data);
                var apnsConfig = FcmConfigs.buildApnsAlertConfig(finalSubject, body, "chat_messages", finalMessageDto.chatId().toString(), true, null, null, imageUrl, null);
                var androidConfig = FcmConfigs.buildAndroidAlertConfig(finalSubject, body, "chat_channel", finalMessageDto.chatId().toString(), 86400000, "OPEN_CHAT", imageUrl);
                var androidDataOnlyConfig = FcmConfigs.buildAndroidDataOnlyConfig(86400000, finalMessageDto.chatId().toString());
                tokens.forEach(t -> {
                    try {
                        notificationService.sendNotificationAsync(t, data,
                                apnsConfig,
                                androidDataOnlyConfig
                        );
                    } catch (FirebaseMessagingException e) {
                        e.printStackTrace();
                    }
                });

            } else response = new ResponseDto<>("Not authorized");

        } catch (Exception ex) {
            response = new ResponseDto<>(ex.getMessage());
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException noTransactionException) {
                response.setMessage(response.getMessage() + " No transaction available");
            }
        }

        return response;
    }

    public ResponseDto<PageResponseDto<MessageDto.List>, Message> findAllByChatId(Long chatId, Pageable pageable) {
        var userDetail = getUserDetails();
        var userId = userDetail.orElse(new CurrentUserDetails()).getId();
        var chatUserOpt = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(chatId, userId);

        if (chatUserOpt.isPresent()) {
            return new ResponseDto<>(new PageResponseDto<>(messageRepository.findAllByChatId(chatId, pageable).map(m -> new MessageDto.List(m.getId(), m.getChatUser().getChat().getId(), userMapper.toDto(m.getChatUser().getUser()), m.getContent(), m.getLabel(), m.getCreatedAt(), m.getUpdatedAt(), userId.equals(m.getChatUser().getUser().getId()), attachmentMapper.toDto(m.getAttachment())))));
        }

        return new ResponseDto<>("Not authorized");
    }

}
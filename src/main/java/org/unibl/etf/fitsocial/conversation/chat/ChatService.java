package org.unibl.etf.fitsocial.conversation.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
import core.util.CurrentUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.FirebaseNotificationService;
import org.unibl.etf.fitsocial.auth.fcmtoken.FcmTokenRepository;
import org.unibl.etf.fitsocial.auth.user.User;
import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.auth.user.UserRepository;
import org.unibl.etf.fitsocial.conversation.attachment.Attachment;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentService;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUser;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserRepository;
import org.unibl.etf.fitsocial.conversation.message.*;
import org.unibl.etf.fitsocial.conversation.message.projection.MessageWithChatId;
import org.yaml.snakeyaml.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService extends BaseSoftDeletableServiceImpl<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create, Long> {
    protected ChatRepository repository;
    protected ChatUserRepository chatUserRepository;
    protected UserRepository userRepository;
    private final MessageRepository messageRepository;
    private AttachmentService attachmentService;
    private FirebaseNotificationService notificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final ObjectMapper objectMapper;

    private MessageMapper messageMapper;

    @Value("base.url")
    private String BASE_URL;

    public ChatService(ChatRepository repository, IMapper<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create> mapper, ChatUserRepository chatUserRepository, UserRepository userRepository, MessageRepository messageRepository, AttachmentService attachmentService, FirebaseNotificationService notificationService, FcmTokenRepository fcmTokenRepository, ObjectMapper objectMapper, MessageMapper messageMapper) {
        super(repository, mapper);
        this.repository = repository;
        this.chatUserRepository = chatUserRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.attachmentService = attachmentService;
        this.notificationService = notificationService;
        this.fcmTokenRepository = fcmTokenRepository;
        this.objectMapper = objectMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public ResponseDto<PageResponseDto<ChatDto.List>, Chat> findAll(Pageable pageable) {
        var userDetails = getUserDetails();
        var userId = userDetails.orElse(new CurrentUserDetails()).getId();

        var chats = repository.findAllChatsByUserId(userId, pageable);
        var chatId = chats.stream().map(Chat::getId).collect(Collectors.toList());
        var chatUsers = chatUserRepository.findAllByChatIdInAndNotUserId(chatId, userId);
        var messages = messageRepository.findLastMessagesForChats(chatId).stream().collect(Collectors.toMap(MessageWithChatId::getChatId, v -> new Tuple<>(v.getContent(), v.getUpdatedAt())));

        var result = chats.stream().map(c -> new ChatDto.List(c.getId(), chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).count() > 1 ? c.getSubject() : chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).map(cu -> cu.getUser().getFirstName() + " " + cu.getUser().getLastName()).collect(Collectors.joining(", ")), messages.get(c.getId()) != null ? messages.get(c.getId())._1() : "", messages.get(c.getId()) != null ? messages.get(c.getId())._2() : c.getUpdatedAt(), chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).map(cu -> new UserDto(cu.getUser().getId(), cu.getUser().getFirstName(), cu.getUser().getLastName())).collect(Collectors.toList()))).collect(Collectors.toList());

        return new ResponseDto<>(new PageResponseDto<>(result, chats.getNumber(), chats.getSize(), chats.getTotalElements(), chats.getTotalPages()));
    }

    @Override
    public ResponseDto<ChatDto, Chat> findById(Long chatId) {
        var userDetails = getUserDetails();
        var userId = userDetails.orElse(new CurrentUserDetails()).getId();
        var chatUser = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(chatId, userId);

        if (chatUser.isEmpty()) return new ResponseDto<>("Not authorized");

        var chatOptional = repository.findById(chatId);

        if (chatOptional.isEmpty()) return new ResponseDto<>(null);

        var chat = chatOptional.get();
        var chatList = Collections.singletonList(chat.getId());
        var chatUsers = chatUserRepository.findAllByChatIdInAndNotUserId(chatList, userId);
        var messages = messageRepository.findLastMessagesForChats(chatList).stream().collect(Collectors.toMap(MessageWithChatId::getChatId, v -> new Tuple<>(v.getContent(), v.getUpdatedAt())));
        return new ResponseDto<>(new ChatDto(chat.getId(), chatUsers.stream().filter(cu -> chat.getId().equals(cu.getChat().getId())).count() > 1 ? chat.getSubject() : chatUsers.stream().filter(cu -> chat.getId().equals(cu.getChat().getId())).map(cu -> cu.getUser().getFirstName() + " " + cu.getUser().getLastName()).collect(Collectors.joining(", ")), messages.get(chat.getId()) != null ? messages.get(chat.getId())._1() : "", messages.get(chat.getId()) != null ? messages.get(chat.getId())._2() : chat.getUpdatedAt(), chatUsers.stream().filter(cu -> chat.getId().equals(cu.getChat().getId())).map(cu -> new UserDto(cu.getUser().getId(), cu.getUser().getFirstName(), cu.getUser().getLastName())).collect(Collectors.toList())));

    }

    @Override
    public ResponseDto<ChatDto, Chat> save(ChatDto.Create dto) {
        ResponseDto<ChatDto, Chat> response;
        try {
            var entity = mapper.fromCreateDto(dto);
            var usersId = new ArrayList<>(dto.userIds());
            var isGroup = usersId.size() > 1;

            if (entity.getSubject() == null || entity.getSubject().isBlank()) {
                var name = userRepository.findAllByIdInAndDeletedAtIsNull(usersId).stream().map(isGroup ? User::getFirstName : user -> user.getFirstName() + " " + user.getLastName()).collect(Collectors.joining(", "));
                entity.setSubject(name);
            }

            var userDetails = getUserDetails();
            var currentUser = entityManager.getReference(User.class, userDetails.orElse(new CurrentUserDetails()).getId());
            entity.setCreatedBy(currentUser);
            var dbEntity = repository.save(entity);

            var charUsers = userRepository.findAllByIdInAndDeletedAtIsNull(usersId).stream().map(user -> {
                ChatUser chatUser = new ChatUser();
                chatUser.setChat(dbEntity);
                chatUser.setUser(user);
                return chatUser;
            }).toList();
            chatUserRepository.saveAllAndFlush(charUsers);

            ChatUser chatUser = new ChatUser();
            chatUser.setChat(dbEntity);
            chatUser.setUser(currentUser);
            chatUserRepository.save(chatUser);

            Attachment attachment;
            if (dto.attachment() != null) {
                var res = attachmentService.save(dto.attachment());
                attachment = res.getEntity();
            } else {
                attachment = null;
            }

            var message = new Message();
            if (attachment != null) message.setAttachment(attachment);
            message.setContent(dto.content());
            message.setChatUser(chatUser);
            var dbMessage = messageRepository.saveAndFlush(message);

            response = findById(dbEntity.getId());

            var tokens = fcmTokenRepository.findAllByUserIdInLatest(dto.userIds());

            var data = new HashMap<String, String>();
            data.put("data", objectMapper.writeValueAsString(response.getResult()));
            data.put("message", objectMapper.writeValueAsString(messageMapper.toDto(dbMessage)));
            data.put("type", "chat");

            tokens.forEach(t -> {
                try {
                    notificationService.sendNotificationAsync(
                            t,
                            chatUser.getUser().getFirstName() + " " + chatUser.getUser().getLastName(),
                            dbMessage.getContent(),
                            attachment != null ? BASE_URL + "/api/attachment/" + attachment.getId() + "/stream" : null,
                            data);
                } catch (FirebaseMessagingException ignored) {
                }
            });


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


    @Override
    public ResponseDto<ChatDto, Chat> update(Long chatId, ChatDto.Update dto) {
        var userDetail = getUserDetails();
        var chatUser = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(chatId, userDetail.orElse(new CurrentUserDetails()).getId());

        if (chatUser.isPresent()) return super.update(chatId, dto);
        else return new ResponseDto<>("Not authorized");
    }

    @Override
    public ResponseDto<ChatDto, Chat> deleteById(Long chatId) {
        var userDetail = getUserDetails();
        var chatUser = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(chatId, userDetail.orElse(new CurrentUserDetails()).getId());

        if (chatUser.isPresent()) return super.deleteById(chatId);

        return new ResponseDto<>("Not authorized");
    }

    public ResponseDto<PageResponseDto<ChatDto.List>, Chat> findByName(String value, Pageable pageable) {
        var userDetails = getUserDetails();
        var userId = userDetails.orElse(new CurrentUserDetails()).getId();

        var chats = repository.findAllChatsByUserIdAndName(userId, value, pageable);
        var chatId = chats.stream().map(Chat::getId).collect(Collectors.toList());
        var chatUsers = chatUserRepository.findAllByChatIdInAndNotUserId(chatId, userId);

        var result = chats.stream().map(c -> new ChatDto.List(c.getId(), chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).count() > 1 ? c.getSubject() : chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).map(cu -> cu.getUser().getFirstName() + " " + cu.getUser().getLastName()).collect(Collectors.joining(", ")), null, null, chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).map(cu -> new UserDto(cu.getUser().getId(), cu.getUser().getFirstName(), cu.getUser().getLastName())).collect(Collectors.toList()))).collect(Collectors.toList());

        return new ResponseDto<>(new PageResponseDto<>(result, chats.getNumber(), chats.getSize(), chats.getTotalElements(), chats.getTotalPages()));
    }
}
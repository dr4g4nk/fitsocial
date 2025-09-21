package org.unibl.etf.fitsocial.conversation.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
import core.util.CurrentUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.auth.fcmtoken.FcmTokenRepository;
import org.unibl.etf.fitsocial.auth.user.User;
import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.auth.user.UserRepository;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentService;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUser;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserRepository;
import org.unibl.etf.fitsocial.conversation.message.MessageMapper;
import org.unibl.etf.fitsocial.conversation.message.MessageRepository;
import org.unibl.etf.fitsocial.conversation.message.projection.MessageWithChatId;
import org.unibl.etf.fitsocial.notification.FirebaseNotificationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService extends BaseSoftDeletableServiceImpl<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create, Long> {
    protected ChatRepository repository;
    protected ChatUserRepository chatUserRepository;
    protected UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final AttachmentService attachmentService;
    private final FirebaseNotificationService notificationService;
    private final FcmTokenRepository fcmTokenRepository;
    private final ObjectMapper objectMapper;

    private final MessageMapper messageMapper;

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
        var result = mapChats(chats, userId);
        return new ResponseDto<>(new PageResponseDto<>(result, chats.getNumber(), chats.getSize(), chats.getTotalElements(), chats.getTotalPages()));
    }

    private java.util.List<ChatDto.List> mapChats(Page<Chat> chats, Long userId) {
        var chatId = chats.stream().map(Chat::getId).collect(Collectors.toList());
        var chatUsers = chatUserRepository.findAllByChatIdInAndNotUserId(chatId, userId);
        var messages = messageRepository.findLastMessagesForChats(chatId).stream()
                .collect(Collectors.toMap(MessageWithChatId::getChatId, v -> v.getSender() + (v.getLabel() != null ? " " + v.getLabel().toLowerCase() : ": " + v.getContent())));

        return chats.stream().map(c ->
                new ChatDto.List(
                        c.getId(),
                        chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).count() > 1
                                ? c.getSubject()
                                : chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId()))
                                .map(cu -> cu.getUser().getFirstName() + " " + cu.getUser().getLastName()).collect(Collectors.joining(", ")),
                        messages.get(c.getId()) != null ? messages.get(c.getId()) : "",
                        c.getLastMessageTime(),
                        chatUsers.stream().filter(cu -> c.getId().equals(cu.getChat().getId())).map(cu -> new UserDto(cu.getUser().getId(), cu.getUser().getFirstName(), cu.getUser().getLastName())).collect(Collectors.toList()))).collect(Collectors.toList());

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
        var messages = messageRepository.findLastMessagesForChats(chatList).stream()
                .collect(Collectors.toMap(MessageWithChatId::getChatId, v -> v.getSender() + (v.getLabel() != null ? " " + v.getLabel().toLowerCase() : ": " + v.getContent())));
        return new ResponseDto<>(
                new ChatDto(
                        chat.getId(),
                        chatUsers.stream().filter(cu -> chat.getId().equals(cu.getChat().getId())).count() > 1
                                ? chat.getSubject()
                                : chatUsers.stream().filter(cu -> chat.getId().equals(cu.getChat().getId()))
                                .map(cu -> cu.getUser().getFirstName() + " " + cu.getUser().getLastName())
                                .collect(Collectors.joining(", ")),
                        messages.get(chat.getId()) != null ? messages.get(chat.getId()) : "",
                        chat.getLastMessageTime(),
                        chatUsers.stream().filter(cu -> chat.getId().equals(cu.getChat().getId())).map(cu -> new UserDto(cu.getUser().getId(), cu.getUser().getFirstName(), cu.getUser().getLastName())).collect(Collectors.toList())));
    }

    @Override
    public ResponseDto<ChatDto, Chat> save(ChatDto.Create dto) {
        ResponseDto<ChatDto, Chat> response;
        try {
            var entity = mapper.fromCreateDto(dto);
            var usersId = new ArrayList<>(dto.userIds());

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

            response = new ResponseDto<>(mapper.toDto(dbEntity), dbEntity);

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

        var result = mapChats(chats, userId);

        return new ResponseDto<>(new PageResponseDto<>(result, chats.getNumber(), chats.getSize(), chats.getTotalElements(), chats.getTotalPages()));
    }
}
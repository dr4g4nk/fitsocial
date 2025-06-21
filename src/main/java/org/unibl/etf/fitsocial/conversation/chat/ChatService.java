package org.unibl.etf.fitsocial.conversation.chat;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.util.CurrentUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.auth.user.User;
import org.unibl.etf.fitsocial.auth.user.UserRepository;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUser;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserRepository;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService extends BaseSoftDeletableServiceImpl<
        Chat,
        ChatDto,
        ChatDto.List,
        ChatDto.Update,
        ChatDto.Create,
        Long
        > {
    protected ChatRepository repository;
    protected ChatUserRepository chatUserRepository;
    protected UserRepository userRepository;


    public ChatService(ChatRepository repository, IMapper<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create> mapper, ChatUserRepository chatUserRepository, UserRepository userRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.chatUserRepository = chatUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto<PageResponseDto<ChatDto.List>, Chat> findAll(Pageable pageable) {
        var userDetails = getUserDetails();
        var userId = userDetails.orElse(new CurrentUserDetails()).getId();
        return new ResponseDto<PageResponseDto<ChatDto.List>, Chat>(new PageResponseDto<ChatDto.List>(repository.findAllChatsByUserId(userId, pageable).map(mapper::toListDto)));
    }

    @Override
    public ResponseDto<ChatDto, Chat> findById(Long chatId) {
        var userDetail = getUserDetails();
        var chatUser = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(chatId, userDetail.orElse(new CurrentUserDetails()).getId());

        if (chatUser.isPresent())
            return super.findById(chatId);
        else return new ResponseDto<>("Not authorized");
    }

    @Override
    public ResponseDto<ChatDto, Chat> save(ChatDto.Create dto) {
        ResponseDto<ChatDto, Chat> response;
        try {
            var entity = mapper.fromCreateDto(dto);
            var usersId = new ArrayList<Long>(dto.userIds());
            var isGroup = usersId.size() > 1;

            if (entity.getSubject() == null || entity.getSubject().isBlank()) {
                var name = userRepository.findAllByIdInAndDeletedAtIsNull(usersId).stream().map(isGroup ? User::getFirstName : user -> user.getFirstName() + " " + user.getLastName()).collect(Collectors.joining(", "));
                entity.setSubject(name);
            }

            var userDetails = getUserDetails();
            var currentUser = entityManager.getReference(User.class, userDetails.orElse(new CurrentUserDetails()).getId());
            entity.setCreatedBy(currentUser);
            var dbEntity = repository.save(entity);

            response = new ResponseDto<ChatDto, Chat>(mapper.toDto(dbEntity), dbEntity);

            usersId.add(entity.getCreatedBy().getId());
            var charUsers = userRepository.findAllByIdInAndDeletedAtIsNull(usersId).stream().map(user -> {
                ChatUser chatUser = new ChatUser();
                chatUser.setChat(dbEntity);
                chatUser.setUser(user);
                return chatUser;
            }).toList();
            chatUserRepository.saveAll(charUsers);
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

        if (chatUser.isPresent())
            return super.update(chatId, dto);
        else return new ResponseDto<>("Not authorized");
    }

    @Override
    public ResponseDto<ChatDto, Chat> deleteById(Long chatId) {
        var userDetail = getUserDetails();
        var chatUser = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(chatId, userDetail.orElse(new CurrentUserDetails()).getId());

        if (chatUser.isPresent())
            return super.deleteById(chatId);

        return new ResponseDto<>("Not authorized");
    }
}
package org.unibl.etf.fitsocial.conversation.message;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.util.CurrentUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.conversation.attachment.Attachment;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;
import org.unibl.etf.fitsocial.conversation.chatuser.ChatUserRepository;

@Service
@Transactional
public class MessageService extends BaseSoftDeletableServiceImpl<
    Message,
    MessageDto,
    MessageDto.List,
    MessageDto.Update,
    MessageDto.Create,
    Long
> {
    protected IMapper<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create> attachmentMapper;
    protected BaseSoftDeletableRepository<Attachment, Long> attachmentRepository;
    protected ChatUserRepository chatUserRepository;

    public MessageService(BaseSoftDeletableRepository<Message, Long> repository,
                          ChatUserRepository chatUserRepository,
                          BaseSoftDeletableRepository<Attachment, Long> attachmentRepository,
                          IMapper<Message, MessageDto, MessageDto.List, MessageDto.Update, MessageDto.Create> mapper,
                          IMapper<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create> attachmentMapper) {
        super(repository, mapper);
        this.attachmentMapper = attachmentMapper;
        this.chatUserRepository = chatUserRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public ResponseDto<MessageDto, Message> save(MessageDto.Create dto) {
        ResponseDto<MessageDto, Message> response;
        try {
            Message entity = mapper.fromCreateDto(dto);

            var userDetail = getUserDetails();
            var chatUser = chatUserRepository.findFirstByChatIdAndUserIdAndDeletedAtIsNull(dto.chatId(), userDetail.orElse(new CurrentUserDetails()).getId());

            if (chatUser.isPresent()) {
                entity.setChatUser(chatUser.get());
                var savedEntity = repository.save(entity);
                response = new ResponseDto<MessageDto, Message>(mapper.toDto(savedEntity), savedEntity);


                var message = response.getEntity();

                if(dto.attachments() != null && !dto.attachments().isEmpty()) {
                    var attachments = dto.attachments().stream().map(attdto -> {
                        var attachment = attachmentMapper.fromCreateDto(attdto);
                        attachment.setMessage(message);
                        return attachment;
                    }).toList();

                    attachmentRepository.saveAll(attachments);
                }
            }
            else  response = new ResponseDto<>("Not authorized");

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

}
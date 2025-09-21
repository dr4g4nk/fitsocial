package org.unibl.etf.fitsocial.conversation.chat;

import core.controller.BaseController;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;
import org.unibl.etf.fitsocial.conversation.message.MessageDto;
import org.unibl.etf.fitsocial.conversation.message.MessageService;

@RestController
@RequestMapping("/api/chat")
public class ChatController extends BaseController<Chat, ChatDto, ChatDto.List, ChatDto.Update, ChatDto.Create, Long> {
    private final ChatService chatService;
    private final MessageService messageService;

    public ChatController(ChatService service, MessageService messageService) {
        super(service);
        this.chatService = service;
        this.messageService = messageService;
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseDto<PageResponseDto<ChatDto.List>, Chat>> filter(@RequestParam String value, Pageable pageable) {
        try {
            return ResponseEntity.ok(chatService.findByName(value, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<ChatDto, Chat>> create(@RequestPart("chat") @Valid ChatDto.Create dto, @RequestPart(value = "attachment", required = false) MultipartFile file) {
        try {
            AttachmentDto.Create attachemnt = null;

            if (file != null) {
                attachemnt = new AttachmentDto.Create(file.getOriginalFilename(), file.getContentType(), file);
            }

            var response = chatService.save(new ChatDto.Create(dto.subject(), dto.userIds(), dto.content(), attachemnt));
            if (response.isSuccess()){
                var chat = response.getEntity();

                if(attachemnt != null || (dto.content() != null && !dto.content().isEmpty()))
                     messageService.save(new MessageDto.Create(chat.getId(), dto.content(), attachemnt));

                response = chatService.findById(chat.getId());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }
}
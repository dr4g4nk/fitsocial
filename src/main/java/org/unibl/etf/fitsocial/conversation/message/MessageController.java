package org.unibl.etf.fitsocial.conversation.message;

import core.controller.BaseController;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitsocial.conversation.attachment.AttachmentDto;
import org.unibl.etf.fitsocial.feed.media.MediaDto;
import org.unibl.etf.fitsocial.feed.post.Post;
import org.unibl.etf.fitsocial.feed.post.PostDto;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/message")
public class MessageController extends BaseController<Message, MessageDto, MessageDto.List, MessageDto.Update, MessageDto.Create, Long> {
    private final MessageService messageService;


    public MessageController(MessageService service) {
        super(service);
        this.messageService = service;
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<ResponseDto<PageResponseDto<MessageDto.List>, Message>> getByChatId(@PathVariable Long chatId, Pageable pageable) {
        try {
            return ResponseEntity.ok(messageService.findAllByChatId(chatId, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<MessageDto, Message>> create(@RequestPart("message") MessageDto.Create dto, @RequestPart(value = "attachment", required = false) MultipartFile attachemnt) {
       try {
           AttachmentDto.Create atta = null;

           if (attachemnt != null) {
                   atta = new AttachmentDto.Create(attachemnt.getOriginalFilename(), attachemnt.getContentType(), attachemnt);;
           }

           var response = messageService.save(new MessageDto.Create(dto.chatId(), dto.content(), atta));

           if (response.isSuccess()) return ResponseEntity.ok(response);
           return ResponseEntity.badRequest().body(response);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(new ResponseDto<>("Error"));
       }
    }
}
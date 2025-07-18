package org.unibl.etf.fitsocial.conversation.attachment;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.unibl.etf.fitsocial.service.FileStorageService;
import org.unibl.etf.fitsocial.util.FileResourceUtil;

import java.io.IOException;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController extends BaseController<
    Attachment,
    AttachmentDto,
    AttachmentDto.List,
    AttachmentDto.Update,
    AttachmentDto.Create,
    Long
> {
    private final FileStorageService fileStorageService;

    public AttachmentController(
            BaseSoftDeletableServiceImpl<Attachment, AttachmentDto, AttachmentDto.List, AttachmentDto.Update, AttachmentDto.Create, Long> service,
            FileStorageService fileStorageService
    ) {
        super(service);
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<StreamingResponseBody> stream(@PathVariable Long id, @RequestHeader HttpHeaders headers) throws IOException {
        try {
            var resp = service.findById(id);
            if (!resp.isSuccess()) {
                return ResponseEntity.notFound().build();
            }
            var attachment = resp.getEntity();

            var util = new FileResourceUtil(fileStorageService);

            var res = util.getResourceResponse(attachment.getFileUrl(), attachment.getContentType(), headers.getRange(), 0);

            return new ResponseEntity<>(res.getBody(), res.getRespHeaders(), res.getStatus());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
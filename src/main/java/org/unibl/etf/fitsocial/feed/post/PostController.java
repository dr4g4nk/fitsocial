package org.unibl.etf.fitsocial.feed.post;

import core.controller.BaseController;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.service.BaseSoftDeletableServiceImpl;
import core.util.CurrentUserDetails;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.unibl.etf.fitsocial.feed.like.Like;
import org.unibl.etf.fitsocial.feed.like.LikeDto;
import org.unibl.etf.fitsocial.feed.like.LikeService;
import org.unibl.etf.fitsocial.feed.media.MediaDto;
import org.unibl.etf.fitsocial.feed.media.MediaService;
import org.unibl.etf.fitsocial.service.FileStorageService;
import org.unibl.etf.fitsocial.util.FileResourceUtil;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/post")
public class PostController extends BaseController<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create, Long> {
    private final PostService postService;
    private final MediaService mediaService;
    private final FileStorageService fileStorageService;
    private final LikeService likeService;

    private static final DefaultDataBufferFactory FACTORY = new DefaultDataBufferFactory();

    public PostController(BaseSoftDeletableServiceImpl<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create, Long> service, PostService postService, MediaService mediaService, FileStorageService fileStorageService, LikeService likeService) {
        super(service);
        this.postService = postService;
        this.mediaService = mediaService;
        this.fileStorageService = fileStorageService;
        this.likeService = likeService;
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<PostDto, Post>> create(@RequestPart("post") PostDto.Create dto, @RequestPart(value = "mediaFiles", required = false) java.util.List<MultipartFile> mediaFiles) {
        try {
            if ((dto.content() == null || dto.content().isBlank()) && mediaFiles.isEmpty())
                return ResponseEntity.badRequest().build();

            var media = new ArrayList<MediaDto.Create>();

            if (mediaFiles != null) {
                for (int i = 0; i < mediaFiles.size(); i++) {
                    var m = mediaFiles.get(i);
                    media.add(new MediaDto.Create(0L, i, m.getContentType(), m, null));
                }
            }

            var response = postService.save(new PostDto.Create(dto.content(), dto.isPublic(), media, dto.activity()));

            if (response.isSuccess()) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<PostDto, Post>> update(@PathVariable Long id, @RequestPart("post") PostDto.Update dto, @RequestPart(value = "mediaOrder", required = false) java.util.List<Integer> mediaOrder, @RequestPart(value = "mediaFiles", required = false) java.util.List<MultipartFile> mediaFiles) {
        try {
            var media = new ArrayList<MediaDto.Update>(dto.media());
            if (mediaFiles != null) {
                if (mediaOrder.size() != mediaFiles.size())
                    return ResponseEntity.badRequest().body(new ResponseDto<>("Param orders and files are not same size"));

                for (int i = 0; i < mediaFiles.size(); i++) {
                    var m = mediaFiles.get(i);
                    media.add(new MediaDto.Update(0L, id, mediaOrder.get(i), m.getContentType(), m));
                }
            }

            var response = postService.update(id, new PostDto.Update(dto.content(), dto.isPublic(), media, dto.activity()));

            if (response.isSuccess()) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseDto<PageResponseDto<PostDto.List>, Post>> getUserPosts(@PathVariable Long id, Pageable pageable) {
        var response = postService.findAllByUserId(id, pageable);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDto<PageResponseDto<PostDto.List>, Post>> getMyPosts(Pageable pageable) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var userDetail = (CurrentUserDetails) auth.getPrincipal();

        var response = postService.findAllByUserId(userDetail.getId(), pageable);
        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    private static final int CHUNK_SIZE = 1024 * 1024; // 1 MB

    @GetMapping("/media/{mediaId}/stream")
    public ResponseEntity<StreamingResponseBody> stream(@PathVariable Long mediaId, @RequestParam(required = false, defaultValue = "false") Boolean thumbnail, @RequestHeader HttpHeaders headers) throws IOException {
        try {
            var resp = mediaService.findById(mediaId);
            if (!resp.isSuccess()) {
                return ResponseEntity.notFound().build();
            }
            var media = resp.getEntity();
            var uri = !thumbnail ? media.getMediaUrl() : media.getThumbnailUrl();
            var mimeType = !thumbnail ? media.getMimeType() : MediaType.IMAGE_JPEG_VALUE;

            var util = new FileResourceUtil(fileStorageService);
            var res = util.getResourceResponse(uri, mimeType, headers.getRange(), 0);

            return new ResponseEntity<>(res.getBody(), res.getRespHeaders(), res.getStatus());
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<ResponseDto<LikeDto, Like>> likePost(@PathVariable Long id) {
        var response = likeService.likePost(id);

        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
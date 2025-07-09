package org.unibl.etf.fitsocial.feed.post;

import core.controller.BaseController;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.service.BaseSoftDeletableServiceImpl;
import core.util.CurrentUserDetails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private static final int BUFFER_SIZE = 64 * 1024; // 64 KB

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<PostDto, Post>> create(@RequestPart("post") PostDto.Create dto, @RequestPart(value = "newMedia", required = false) java.util.List<MultipartFile> newMedia) {
        var media = new ArrayList<MediaDto.Create>();

        if (newMedia != null) {
            for (int i = 0; i < newMedia.size(); i++) {
                var m = newMedia.get(i);
                media.add(new MediaDto.Create(0L, i, m.getContentType(), m, null));
            }
        }

        var response = postService.save(new PostDto.Create(dto.content(), dto.isPublic(), media));

        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<PostDto, Post>> update(@PathVariable Long id, @RequestPart("post") PostDto.Update dto, @RequestPart(value = "mediaOrder", required = false) java.util.List<Integer> mediaOrder, @RequestPart(value = "mediaFiles", required = false) java.util.List<MultipartFile> mediaFiles) {
        var media = new ArrayList<MediaDto.Update>(dto.media());
        if (mediaFiles != null) {
            if (mediaOrder.size() != mediaFiles.size())
                return ResponseEntity.badRequest().body(new ResponseDto<>("Param orders and files are not same size"));

            for (int i = 0; i < mediaFiles.size(); i++) {
                var m = mediaFiles.get(i);
                media.add(new MediaDto.Update(0L, id, mediaOrder.get(i), m.getContentType(), m));
            }
        }

        var response = postService.update(id, new PostDto.Update(dto.content(), dto.isPublic(), media));

        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
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
    public ResponseEntity<StreamingResponseBody> stream(@PathVariable Long mediaId, @RequestHeader HttpHeaders headers) throws IOException {
        var resp = mediaService.findById(mediaId);
        if (!resp.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        var media = resp.getEntity();
        Resource resource = fileStorageService.loadAsResource(media.getMediaUrl());
        long fileSize = resource.contentLength();

        MediaType mediaType = MediaType.parseMediaType(media.getMimeType());

        boolean isVideo = mediaType.getType().equals("video");
        List<HttpRange> ranges = headers.getRange();
        long start, end = fileSize - 1;
        HttpStatus status = HttpStatus.OK;

        if (isVideo){
            if(!ranges.isEmpty()) {
                HttpRange range = ranges.getFirst();
                start = range.getRangeStart(fileSize);
                end = range.getRangeEnd(fileSize);

            } else {
                start = 0;
                end = fileSize < CHUNK_SIZE ? fileSize - 1 : CHUNK_SIZE;
            }

            status = HttpStatus.PARTIAL_CONTENT;
        } else {
            start = 0;
        }

        long contentLength = end - start + 1;

        StreamingResponseBody body = outputStream -> {
            try (SeekableByteChannel channel = Files.newByteChannel(resource.getFile().toPath(), StandardOpenOption.READ)) {
                channel.position(start);
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                long bytesLeft = contentLength;

                while (bytesLeft > 0) {
                    int read = channel.read(buffer);
                    if (read == -1) break;
                    buffer.flip();

                    int toWrite = (int)Math.min(read, bytesLeft);
                    outputStream.write(buffer.array(), 0, toWrite);
                    bytesLeft -= toWrite;
                    buffer.clear();
                }
            }
        };

        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.set(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        respHeaders.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        respHeaders.setContentLength(contentLength);
        respHeaders.setCacheControl(cacheControl);

        if (status == HttpStatus.PARTIAL_CONTENT) {
            respHeaders.set(
                    HttpHeaders.CONTENT_RANGE,
                    String.format("bytes %d-%d/%d", start, end, fileSize)
            );
        }

        return new ResponseEntity<>(body, respHeaders, status);
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<ResponseDto<LikeDto, Like>> likePost(@PathVariable Long id) {
        var response = likeService.likePost(id);

        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
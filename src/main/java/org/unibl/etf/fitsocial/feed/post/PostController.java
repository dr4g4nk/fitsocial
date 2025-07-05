package org.unibl.etf.fitsocial.feed.post;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.util.CurrentUserDetails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.fitsocial.feed.like.Like;
import org.unibl.etf.fitsocial.feed.like.LikeDto;
import org.unibl.etf.fitsocial.feed.like.LikeService;
import org.unibl.etf.fitsocial.feed.media.MediaDto;
import org.unibl.etf.fitsocial.feed.media.MediaService;
import org.unibl.etf.fitsocial.service.FileStorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/post")
public class PostController extends BaseController<
        Post,
        PostDto,
        PostDto.List,
        PostDto.Update,
        PostDto.Create,
        Long
        > {
    private final PostService postService;
    private final MediaService mediaService;
    private final FileStorageService fileStorageService;
    private final LikeService likeService;

    public PostController(BaseSoftDeletableServiceImpl<Post, PostDto, PostDto.List, PostDto.Update, PostDto.Create, Long> service, PostService postService, MediaService mediaService, FileStorageService fileStorageService, LikeService likeService) {
        super(service);
        this.postService = postService;
        this.mediaService = mediaService;
        this.fileStorageService = fileStorageService;
        this.likeService = likeService;
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<PostDto, Post>> create(@RequestPart("post") PostDto.Create dto,
                                                             @RequestPart(value = "newMedia", required = false)
                                                             java.util.List<MultipartFile> newMedia){
        var media = new ArrayList<MediaDto.Create>();

        if(newMedia != null) {
            for (int i = 0; i < newMedia.size(); i++) {
                var m = newMedia.get(i);
                media.add(new MediaDto.Create(0L, i, m.getContentType(), m, null));
            }
        }

        var response = postService.save(new PostDto.Create(dto.content(), dto.isPublic(), media));

        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<PostDto, Post>> update(@PathVariable Long id,
                                                             @RequestPart("post") PostDto.Update dto,
                                                             @RequestPart(value = "mediaOrder", required = false)
                                                             java.util.List<Integer> mediaOrder,
                                                             @RequestPart(value = "mediaFiles", required = false)
                                                             java.util.List<MultipartFile> mediaFiles){
        var media = new ArrayList<MediaDto.Update>(dto.media());
        if(mediaFiles != null) {
            if (mediaOrder.size() != mediaFiles.size())
                return ResponseEntity.badRequest().body(new ResponseDto<>("Param orders and files are not same size"));

            for (int i = 0; i < mediaFiles.size(); i++) {
                var m = mediaFiles.get(i);
                media.add(new MediaDto.Update(0L, id, mediaOrder.get(i), m.getContentType(), m));
            }
        }

        var response = postService.update(id, new PostDto.Update(dto.content(), dto.isPublic(), media));

        if(response.isSuccess()) return ResponseEntity.ok(response);
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

    private ResourceRegion getResourceRegion(Resource resource,
                                             HttpHeaders headers,
                                             long contentLength) throws IOException {
        List<HttpRange> ranges = headers.getRange();
        if (ranges.isEmpty()) {
            long chunkLength = Math.min(CHUNK_SIZE, contentLength);
            return new ResourceRegion(resource, 0, chunkLength);
        }
        // podržavamo samo prvi Range
        HttpRange range = ranges.getFirst();
        long start = range.getRangeStart(contentLength);
        long end   = range.getRangeEnd(contentLength);
        long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
        return new ResourceRegion(resource, start, rangeLength);
    }

    private static final long CHUNK_SIZE = 1024 * 1024; // 1 MB

    @GetMapping("/media/{mediaId}/stream")
    public ResponseEntity<ResourceRegion> stream(@PathVariable Long mediaId, @RequestHeader HttpHeaders headers) throws IOException {
        var response = mediaService.findById(mediaId);
        if (response.isSuccess()) {
            var media = response.getEntity();
            var mimeType = media.getMimeType();
            Resource resource = fileStorageService.loadAsResource(media.getMediaUrl());

            CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);

            if (mimeType.startsWith("video")) {
                long contentLength = resource.contentLength();
                ResourceRegion region = getResourceRegion(resource, headers, contentLength);

                HttpStatus status = headers.getRange().isEmpty()
                        ? HttpStatus.OK
                        : HttpStatus.PARTIAL_CONTENT;

                return ResponseEntity.status(status)
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .contentType(MediaType.parseMediaType(mimeType))
                        .cacheControl(cacheControl)
                        .body(region);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .cacheControl(cacheControl)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(new ResourceRegion(resource, 0, resource.contentLength()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ResponseDto<LikeDto, Like>> likePost(@PathVariable Long id) {
        var response = likeService.likePost(id);

        if (response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
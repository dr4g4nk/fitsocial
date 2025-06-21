package org.unibl.etf.fitsocial.feed.like;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;

@RestController
@RequestMapping("/api/like")
public class LikeController extends BaseController<
    Like,
    LikeDto,
    LikeDto.List,
    LikeDto.Update,
    LikeDto.Create,
    Long
> {
    protected LikeService service;
    public LikeController(LikeService service) {
        super(service);

        this.service = service;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseDto<PageResponseDto<LikeDto.List>, Like>> ListDtoByPostId(@PathVariable Long postId, Pageable pageable) {
        var response = service.findAllByPostId(postId, pageable);

        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }
}
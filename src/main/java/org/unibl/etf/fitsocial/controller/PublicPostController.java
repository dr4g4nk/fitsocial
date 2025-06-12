package org.unibl.etf.fitsocial.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.dto.PostDto;
import org.unibl.etf.fitsocial.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PublicPostController {

    protected PostService postService;


    public PublicPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("posts")
    public Page<PostDto> getAll(Pageable pageable) {
        return postService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dto> getById(@PathVariable ID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

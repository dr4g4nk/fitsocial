package core.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.service.IBaseService;

public class BaseController<T, Dto, ListDto, UpdateDto, CreateDto, ID> {

    protected final IBaseService<T, Dto, ListDto, UpdateDto, CreateDto, ID> service;

    public BaseController(IBaseService<T, Dto, ListDto, UpdateDto, CreateDto, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageResponseDto<ListDto>, T>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Dto, T>> getById(@PathVariable ID id) {
        var response = service.findById(id);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Dto, T>> create(@RequestBody @Valid CreateDto dto) {
        var response = service.save(dto);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Dto, T>> update(@PathVariable ID id, @RequestBody @Valid UpdateDto dto) {
        var response = service.update(id, dto);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
package core.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.service.IBaseService;

@RestControllerAdvice
public class BaseController<T, Dto, ListDto, UpdateDto, CreateDto, ID> {

    protected final IBaseService<T, Dto, ListDto, UpdateDto, CreateDto, ID> service;

    public BaseController(IBaseService<T, Dto, ListDto, UpdateDto, CreateDto, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageResponseDto<ListDto>, T>> getAll(Pageable pageable) {
        try {
            return ResponseEntity.ok(service.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Dto, T>> getById(@PathVariable ID id) {
        try {
            var response = service.findById(id);
            if (response.isSuccess()) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Dto, T>> create(@RequestBody @Valid CreateDto dto) {
        try {
            var response = service.save(dto);
            if (response.isSuccess()) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Dto, T>> update(@PathVariable ID id, @RequestBody @Valid UpdateDto dto) {
        try {
            var response = service.update(id, dto);
            if (response.isSuccess()) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Dto, T>> delete(@PathVariable ID id) {
        try {
            var response = service.deleteById(id);
            if (response.isSuccess()) return ResponseEntity.ok(response);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage()));
        }
    }
}
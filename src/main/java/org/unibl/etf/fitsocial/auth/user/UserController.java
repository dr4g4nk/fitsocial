package org.unibl.etf.fitsocial.auth.user;

import core.dto.ResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.controller.BaseController;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.web.server.ResponseStatusException;
import org.unibl.etf.fitsocial.service.FileStorageService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController<
    User,
    UserDto,
    UserDto.List,
    UserDto.Update,
    UserDto.Create,
    Long
> {
    private final UserService userService;
    private final FileStorageService fileStorageService;

    public UserController(BaseSoftDeletableServiceImpl<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create, Long> service, UserService userService, FileStorageService fileStorageService) {
        super(service);
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseEntity<ResponseDto<UserDto, User>> create(UserDto.Create userDto) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied!");
    }

    @Override
    public ResponseEntity<ResponseDto<UserDto, User>> update(Long aLong, UserDto.Update userDto) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied!");
    }

    @Override
    public ResponseEntity<ResponseDto<UserDto, User>> delete(Long aLong) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied!");
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable Long id) {
        var response = userService.findById(id);
        if(response.isSuccess()) {
            var user = response.getEntity();
            var contentType = user.getProfileImageContentType();
            Resource resource = fileStorageService.loadAsResource(user.getProfileImageUrl());

            CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .cacheControl(cacheControl)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

}
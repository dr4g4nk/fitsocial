package org.unibl.etf.fitsocial.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import core.controller.BaseController;
import org.unibl.etf.fitsocial.dto.UserDto;
import core.dto.ResponseDto;
import org.unibl.etf.fitsocial.entity.User;
import org.unibl.etf.fitsocial.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create, Long> {
    public UserController(UserService service) {
        super(service);
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
    public ResponseEntity<Void> delete(Long aLong) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied!");
    }
}

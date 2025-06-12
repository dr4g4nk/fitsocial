package org.unibl.etf.fitsocial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.fitsocial.controller.base.BaseController;
import org.unibl.etf.fitsocial.dto.UserDto;
import org.unibl.etf.fitsocial.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<UserDto, Long> {
    public UserController(UserService service) {
        super(service);
    }
}

package org.unibl.etf.fitsocial.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitsocial.dto.UserDto;
import org.unibl.etf.fitsocial.entity.User;
import org.unibl.etf.fitsocial.service.base.BaseServiceImpl;
import org.unibl.etf.fitsocial.service.base.IBaseService;

@Service
public class UserService extends BaseServiceImpl<User, UserDto, Long> {
    public UserService(JpaRepository<User, Long> repository) {
        super(repository);
    }

    @Override
    protected User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        return user;
    }

    @Override
    protected UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getDateOfBirth());
    }
}

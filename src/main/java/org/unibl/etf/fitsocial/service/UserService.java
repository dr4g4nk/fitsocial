package org.unibl.etf.fitsocial.service;

import core.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.dto.UserDto;
import org.unibl.etf.fitsocial.entity.User;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.unibl.etf.fitsocial.repository.UserRepository;

@Service
@Transactional
public class UserService extends BaseSoftDeletableServiceImpl<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create, Long> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public UserService(BaseSoftDeletableRepository<User, Long> repository, IMapper<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create> mapper) {
        super(repository, mapper);
    }

    @Override
    public ResponseDto<UserDto, User> save(UserDto.Create dto) {
        ResponseDto<UserDto, User> response;
        try {
            // Provjeri da li već postoji username/email
            if (userRepository.existsByUsernameAndDeletedAtIsNull(dto.username()))
                return new ResponseDto<>("Username already exists");
            if (userRepository.existsByEmailAndDeletedAtIsNull(dto.email()))
                return new ResponseDto<>("Email already exists");

            UserDto.Create newDto = new UserDto.Create(dto.firstName(), dto.lastName(), dto.username(), dto.email(), passwordEncoder.encode(dto.password()));

            response = super.save(newDto);
            var user = response.getEntity();

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException noTransactionException) {
                response.setMessage(response.getMessage() + " No transaction available");
            }
        }

        return response;
    }
}
package org.unibl.etf.fitsocial.auth.user;

import core.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Transactional
public class UserService extends BaseSoftDeletableServiceImpl<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create, Long> {

    private PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public UserService(BaseSoftDeletableRepository<User, Long> repository,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       IMapper<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create> mapper) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
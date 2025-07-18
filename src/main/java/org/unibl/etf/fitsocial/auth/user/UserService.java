package org.unibl.etf.fitsocial.auth.user;

import core.dto.PageResponseDto;
import core.dto.ResponseDto;
import core.util.CurrentUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import core.mapper.IMapper;
import core.repository.BaseSoftDeletableRepository;
import core.service.BaseSoftDeletableServiceImpl;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.unibl.etf.fitsocial.auth.FcmTokenDto;
import org.unibl.etf.fitsocial.auth.fcmtoken.FcmToken;
import org.unibl.etf.fitsocial.auth.fcmtoken.FcmTokenRepository;

import java.time.Instant;

@Service
@Transactional
public class UserService extends BaseSoftDeletableServiceImpl<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create, Long> {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private FcmTokenRepository fcmTokenRepository;

    public UserService(BaseSoftDeletableRepository<User, Long> repository,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       IMapper<User, UserDto, UserDto.List, UserDto.Update, UserDto.Create> mapper, FcmTokenRepository fcmTokenRepository) {
        super(repository, mapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fcmTokenRepository = fcmTokenRepository;
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

    public void saveFcmToken(FcmTokenDto dto){
        var userDetails = getUserDetails();
        var userId = userDetails.orElse(new CurrentUserDetails()).getId();
        var user = entityManager.getReference(User.class, userId);

        var optToken = fcmTokenRepository.findByToken(dto.token());
        if(optToken.isPresent()){
            var fcmToken = optToken.get();
            if(!userId.equals(fcmToken.getUser().getId())){
                fcmToken.setUser(user);
                fcmToken.setTimestamp(Instant.now());

                fcmTokenRepository.save(fcmToken);
            }
        }
        else{
            var fcmToken = new FcmToken();
            fcmToken.setToken(dto.token());
            fcmToken.setUser(user);
            fcmToken.setTimestamp(Instant.now());

            fcmTokenRepository.save(fcmToken);
        }

    }

    public ResponseDto<PageResponseDto<UserDto.List>, User> findByValue(String value, Pageable pageable) {
        var res = userRepository.findAllByTextFilter(value, pageable);

        return new ResponseDto<>(new PageResponseDto<>(res.map(mapper::toListDto)));
    }

    public void logout(){
        var userDetails = getUserDetails();
        var userId = userDetails.orElse(new CurrentUserDetails()).getId();

        fcmTokenRepository.deleteByUserId(userId);
    }
}
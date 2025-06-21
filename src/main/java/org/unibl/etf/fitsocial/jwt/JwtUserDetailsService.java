package org.unibl.etf.fitsocial.jwt;

import core.util.CurrentUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitsocial.auth.user.UserRepository;
import org.unibl.etf.fitsocial.auth.userrole.UserRoleRepository;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public JwtUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByUsernameAndDeletedAtIsNull(username);
        if (user == null) throw new UsernameNotFoundException("Not found");

        return new CurrentUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user.getId())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Long userId) {
        return userRoleRepository.findByUserIdAndDeletedAtIsNull(userId).stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRole().getName()))
                .collect(Collectors.toList());
    }
}

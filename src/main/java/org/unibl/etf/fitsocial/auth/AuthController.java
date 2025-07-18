package org.unibl.etf.fitsocial.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.jwt.JwtUserDetailsService;
import org.unibl.etf.fitsocial.jwt.JwtUtil;
import org.unibl.etf.fitsocial.auth.user.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = null;
        try {
            var username = "";
            if(authRequest.getUsername() == null ) username = authRequest.getEmail();
            else username = authRequest.getUsername();
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

            var userDetails = (UserDetails) authentication.getPrincipal();

            final var tokens = jwtUtil.generateTokens(userDetails.getUsername());
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout(){
        try{
            userService.logout();
        } catch (Exception e){ }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto.Create dto) {
        var response = userService.save(dto);
        if(response.isSuccess()) return ResponseEntity.ok(response);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest req) {
        JwtUtil.TokenResponse tokens = jwtUtil.refreshTokens(req.refreshToken());
        return ResponseEntity.ok(tokens);
    }
}

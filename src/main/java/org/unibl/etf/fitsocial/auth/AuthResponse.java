package org.unibl.etf.fitsocial.auth;

import org.unibl.etf.fitsocial.auth.user.UserDto;

public record AuthResponse(String token, String refreshToken, UserDto user) {}

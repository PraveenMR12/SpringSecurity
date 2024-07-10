package org.dreamorbit.employee.service;

import org.dreamorbit.employee.entity.User;
import org.dreamorbit.employee.payload.JwtResponse;
import org.dreamorbit.employee.payload.RefreshTokenRequest;
import org.dreamorbit.employee.payload.SignUpRequest;
import org.dreamorbit.employee.payload.SigninRequest;

public interface AuthenticationService {
	public User signUp(SignUpRequest signUpRequest);
	
	public JwtResponse signin(SigninRequest signinRequest);
	
	public JwtResponse refreshToken(RefreshTokenRequest refreshToken);
}

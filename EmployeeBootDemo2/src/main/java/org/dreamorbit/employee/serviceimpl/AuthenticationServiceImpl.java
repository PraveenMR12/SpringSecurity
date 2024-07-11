package org.dreamorbit.employee.serviceimpl;

import java.util.HashMap;

import org.dreamorbit.employee.entity.User;
import org.dreamorbit.employee.payload.JwtResponse;
import org.dreamorbit.employee.payload.RefreshTokenRequest;
import org.dreamorbit.employee.payload.SignUpRequest;
import org.dreamorbit.employee.payload.SigninRequest;
import org.dreamorbit.employee.repository.UserRepository;
import org.dreamorbit.employee.security.JwtHelper;
import org.dreamorbit.employee.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtHelper jwtService;
	
	
	@Override
	public User signUp(SignUpRequest signUpRequest) {
		User user = new User();
		user.setEmail(signUpRequest.getEmail());
		user.setName(signUpRequest.getName());
		user.setRole("User");
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		
		return userRepository.save(user);
		
	}
	
	
	public JwtResponse signin(SigninRequest signinRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
		var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(()->new BadCredentialsException("Invalid Credintials!"));
		var jwt = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setJwtToken(jwt);
		jwtResponse.setRefreshToken(refreshToken);
		
		return jwtResponse;
		
	}
	
	
	public JwtResponse refreshToken(RefreshTokenRequest refreshToken) {
		String userEmail = jwtService.extractUserName(refreshToken.getToken());
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(()->new BadCredentialsException("Invalid Credintials!"));
		if(jwtService.isTokenValid(refreshToken.getToken(), user)) {
			var jwt = jwtService.generateToken(user);
			JwtResponse jwtResponse = new JwtResponse();
			jwtResponse.setJwtToken(jwt);
			jwtResponse.setRefreshToken(refreshToken.getToken());
			return jwtResponse;
		}
		return null;
		
	}
	
}

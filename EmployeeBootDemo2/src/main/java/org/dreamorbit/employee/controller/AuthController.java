package org.dreamorbit.employee.controller;

import org.dreamorbit.employee.entity.User;
import org.dreamorbit.employee.payload.JwtResponse;
import org.dreamorbit.employee.payload.RefreshTokenRequest;
import org.dreamorbit.employee.payload.SignUpRequest;
import org.dreamorbit.employee.payload.SigninRequest;
import org.dreamorbit.employee.security.JwtHelper;
import org.dreamorbit.employee.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

//	@Autowired
//	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationService authService;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JwtHelper helper;

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/signUp")
	public ResponseEntity<User> signUp(@RequestBody SignUpRequest request) {



		return new ResponseEntity<>(authService.signUp(request), HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> signin(@RequestBody SigninRequest request) {
		
		return ResponseEntity.ok(authService.signin(request));
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshTokenRequest refreshToken) {
		
		return ResponseEntity.ok(authService.refreshToken(refreshToken));
	}
	
	

//	private void doAuthenticate(String email, String password) {
//
//		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
//		try {
//			System.out.println(email + " ," + password);
//			manager.authenticate(authentication);
//
//		} catch (BadCredentialsException e) {
//			throw new BadCredentialsException(" Invalid Username or Password  !!");
//		}
//
//	}

	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Credentials Invalid !!";
	}

}

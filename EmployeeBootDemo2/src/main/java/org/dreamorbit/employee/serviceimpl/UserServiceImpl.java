package org.dreamorbit.employee.serviceimpl;

import org.dreamorbit.employee.exception.EmployeeResourceException;
import org.dreamorbit.employee.repository.UserRepository;
import org.dreamorbit.employee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetailsService userDetailsService() {

		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepo.findByEmail(username)
						.orElseThrow(()->new EmployeeResourceException("User not found"));
			}
		};
	}
}

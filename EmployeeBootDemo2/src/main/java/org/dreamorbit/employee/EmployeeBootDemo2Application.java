package org.dreamorbit.employee;

import org.dreamorbit.employee.entity.User;
import org.dreamorbit.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EmployeeBootDemo2Application implements CommandLineRunner{

	@Autowired
	private UserRepository userRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(EmployeeBootDemo2Application.class, args);
	}

	public void run(String...args) {
		User adminAccount  = userRepo.findByRole("Admin");
		if(null== adminAccount) {
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setName("admin");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setRole("Admin");
			
			userRepo.save(user);
		}
	}
	
}

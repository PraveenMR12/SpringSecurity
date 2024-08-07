package org.dreamorbit.employee.repository;

import java.util.Optional;

import org.dreamorbit.employee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);
	
	User findByRole(String role);
	
}

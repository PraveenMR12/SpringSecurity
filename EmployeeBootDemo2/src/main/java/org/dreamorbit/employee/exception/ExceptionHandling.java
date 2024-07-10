package org.dreamorbit.employee.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling {
	
	
	@ExceptionHandler(value = EmployeeResourceException.class)
	public ResponseEntity<Map<String, String>> employeeResourceException(EmployeeResourceException ere){
		
		Map<String, String> expRes = new HashMap<>();
		String message = ere.getMessage();
		expRes.put("Error", message);

		return new ResponseEntity<Map<String, String>>(expRes, HttpStatus.NOT_FOUND);
	}

}

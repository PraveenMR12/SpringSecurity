package org.dreamorbit.springbootdemo.controller;

import java.security.Principal;
import java.util.List;

import org.dreamorbit.springbootdemo.entity.Employee;
import org.dreamorbit.springbootdemo.payload.ApiResponse;
import org.dreamorbit.springbootdemo.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class EmployeeController {

	@Autowired
	EmployeeServiceImpl service;
	
	//create employee
	@PostMapping("/createEmployee")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		return new ResponseEntity<Employee>(service.addEmployee(employee), HttpStatus.CREATED);
	}

	
	//read employee
	@GetMapping("/getEmployee/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
		return new ResponseEntity<Employee>(service.getEmployess(id), HttpStatus.OK);
	}
	
	
	//read all employee
	@GetMapping("/getEmployee")
	public ResponseEntity<List<Employee>> getAllEmployee() {
		return new ResponseEntity<List<Employee>>(service.getAllEmployee(), HttpStatus.OK) ;
	}
	
	@GetMapping("/getUser")
	public ResponseEntity<ApiResponse> getUser(Principal principle) {
		return ResponseEntity.ok(new ApiResponse("CurrentUser - "+principle.getName(), true));
	}

	
	//update employee
	@PutMapping("/updateEmployee")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
		return ResponseEntity.ok(service.updateEmployee(employee));
	}	

	
	//delete employee
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<ApiResponse> removeEmployee(@PathVariable int id) {
		return ResponseEntity.ok(new ApiResponse(service.deleteEmployee(id), true));
	}

}

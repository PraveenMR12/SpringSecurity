package org.dreamorbit.springbootdemo.service;

import java.util.List;

import org.dreamorbit.springbootdemo.entity.Employee;

public interface EmployeeService {
	
	
	public Employee addEmployee(Employee employee);
	
	public Employee getEmployess(int id);
	
	public List<Employee> getAllEmployee();
	
	public Employee updateEmployee(Employee employee);
	
	public String deleteEmployee(int id);
	

}

package org.dreamorbit.springbootdemo.service;

import java.util.List;

import org.dreamorbit.springbootdemo.Repository.EmployeeRepository;
import org.dreamorbit.springbootdemo.entity.Employee;
import org.dreamorbit.springbootdemo.exceptions.EmployeeResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepository repo;

	public Employee addEmployee(Employee employee) {
		Employee emp = repo.findById(employee.getId())
				.orElseThrow(()->new EmployeeResourceException("Id is Already Present in Database"));
		
		return repo.save(emp);
	}

	public Employee getEmployess(int id) {
		
		Employee emp = repo.findById(id)
				.orElseThrow(()->new EmployeeResourceException("Id Not Present in DataBase"));
			return emp;
	}

	public List<Employee> getAllEmployee() {
		
		return repo.findAll();
	}

	public Employee updateEmployee(Employee employee) {
		Employee existingEmp = repo.findById(employee.getId())
				.orElseThrow(()->new EmployeeResourceException("Id Not Present in DataBase"));
		
		existingEmp.setName(employee.getName());
		existingEmp.setDept(employee.getDept());
		existingEmp.setSalary(employee.getSalary());
		return repo.save(existingEmp);
	}

	public String deleteEmployee(int id) {
		Employee emp = repo.findById(id)
				.orElseThrow(()->new EmployeeResourceException("Id Not Present in DataBase"));
		repo.delete(emp);
		return "Employee of Id "+id+" is deleted";
	}
	
	
}

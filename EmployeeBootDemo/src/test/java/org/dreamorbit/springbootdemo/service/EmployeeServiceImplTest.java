package org.dreamorbit.springbootdemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.dreamorbit.springbootdemo.Repository.EmployeeRepository;
import org.dreamorbit.springbootdemo.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class EmployeeServiceImplTest {
	
	
	@Mock
	private EmployeeRepository repo;
	
	@InjectMocks
	private EmployeeServiceImpl service;
	
	Employee emp1;
	Employee emp2;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		emp1 = Employee.builder()
				.id(1)
				.name("A")
				.dept("aa")
				.salary(11111).build();
		emp2 = Employee.builder()
				.id(2)
				.name("B")
				.dept("aa")
				.salary(11111).build();
	}

	@Test
	void testAddEmployee() {
		
		when(repo.findById(1)).thenReturn(Optional.of(emp1));
		when(repo.save(emp1)).thenReturn(emp1);
		
		assertThat(service.addEmployee(emp1)).isNotNull();
		assertThat(service.addEmployee(emp1).getId()).isEqualTo(1);
	}

	@Test
	void testGetEmployess() {
		
		when(repo.findById(1)).thenReturn(Optional.of(emp1));
		
		assertThat(service.getEmployess(1)).isNotNull();
		assertThat(service.getEmployess(1).getId()).isGreaterThan(0);
	}

	@Test
	void testGetAllEmployee() {
		
		when(repo.findAll()).thenReturn(Arrays.asList(emp1, emp2));
		
		assertThat(service.getAllEmployee().size()).isGreaterThan(0);
		assertThat(service.getAllEmployee().get(1).getName()).isEqualTo("B");

	}

	@Test
	void testUpdateEmployee() {
		
		when(repo.findById(1)).thenReturn(Optional.of(emp1));
		when(repo.save(emp1)).thenReturn(emp1);
		
		assertThat(service.updateEmployee(emp1)).isNotNull();
		assertThat(service.updateEmployee(emp1).getId()).isGreaterThan(0);
	}

	@Test
	void testDeleteEmployee() {
		when(repo.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(emp1));
		doNothing().when(repo).delete(emp1);
		
		assertEquals("Employee of Id 1 is deleted", service.deleteEmployee(1));
	}

}

package org.dreamorbit.springbootdemo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.dreamorbit.springbootdemo.entity.Employee;
import org.dreamorbit.springbootdemo.exceptions.EmployeeResourceException;
import org.dreamorbit.springbootdemo.payload.ApiResponse;
import org.dreamorbit.springbootdemo.service.EmployeeService;
import org.dreamorbit.springbootdemo.service.EmployeeServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EmployeeController.class)
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	EmployeeController controller;
	@MockBean
	EmployeeServiceImpl service;
	
	Employee employee;


	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.employee= Employee.builder()
						.id(1)
						.name("A")
						.dept("aa")
						.salary(11111).build();
	}
	
	@Test
	void testAddEmployee() throws JsonProcessingException, Exception {
		
		when(service.addEmployee(ArgumentMatchers.any()))
		.thenAnswer(ans->ans.getArgument(0));
		
		assertThat(controller.addEmployee(employee).getBody().getId()).isEqualTo(1);
//		BDDMockito.given(service.addEmployee(ArgumentMatchers.any())).willAnswer(in -> in.getArgument(0));
		
		
		ResultActions response = mockMvc.perform(post("/createEmployee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));
		
			response.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(employee.getId())))
//			.andDo(MockMvcResultHandlers.print())
			;
	}

	@Test
	void testGetEmployee() throws Exception{
		when(service.getEmployess(ArgumentMatchers.anyInt()))
		.thenAnswer(ans->(ans.getArgument(0).equals(employee.getId()))?employee:null);
		
		assertEquals(controller.getEmployee(1).getBody().getName(), "A");
		assertThrows(NullPointerException.class, ()->controller.getEmployee(2).getBody().getName());
		
		ResultActions response = mockMvc.perform(get("/getEmployee/1")
				.contentType(MediaType.APPLICATION_JSON)
				.param("id","1"));
		
		response.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("A")))
		.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testGetAllEmployee() throws Exception
	{
		when(service.getAllEmployee()).thenReturn(Arrays.asList(employee));
		
		assertThat(controller.getAllEmployee().getBody().get(0).getName())
		.isEqualTo("A");
		
		ResultActions response = mockMvc.perform(get("/getEmployee")
				.contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(employee.getName())));
	}

	@Test
	void testUpdateEmployee() throws Exception{
		when(service.updateEmployee(ArgumentMatchers.any()))
		.thenAnswer(ans->ans.getArgument(0));
		
		assertThat(controller.updateEmployee(employee).getBody().getDept())
		.isEqualTo("aa");
		
		ResultActions response = mockMvc.perform(put("/updateEmployee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee))
				);
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("A"))
		;
		
	}

	@Test
	void testRemoveEmployee() throws Exception {
		when(service.deleteEmployee(ArgumentMatchers.anyInt()))
		.thenAnswer(ans -> "Employee of Id "+ans.getArgument(0)+" is deleted");
		
		assertThat(controller.removeEmployee(1).getBody().isSuccess()).isEqualTo(true);
		
		ResultActions response = mockMvc.perform(delete("/deleteEmployee/{id}",1)
				.contentType(MediaType.APPLICATION_JSON)
				);
		response.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.success", CoreMatchers.is(true)));
				
	}

}

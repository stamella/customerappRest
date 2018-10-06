package com.capgemini.customerapp;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capgemini.customerapp.controller.CustomerController;
import com.capgemini.customerapp.entity.Customer;
import com.capgemini.customerapp.service.CustomerService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CustomerControllerTest {

	MockMvc mockMvc;

	@Mock
	CustomerService customerService;

	@InjectMocks
	CustomerController customerController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
	}

	@Test
	public void testAuthenticateCustomer() throws Exception {
		String content = "{\r\n" + "  \"customerId\": 100,\r\n" + "  \"customerPassword\": \"swa\"\r\n" + "}";
		Customer customer = new Customer(100, "swathi", "swathi@gmail.com", "Hyderabad", "swa");
		when(customerService.authentication(Mockito.isA(Customer.class))).thenReturn(customer);
		mockMvc.perform(post("/v1/auth").content(content).contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print()).andExpect(jsonPath("$.customerEmail").exists())
				.andExpect(jsonPath("$.customerEmail").value("swathi@gmail.com"));
		verify(customerService).authentication(Mockito.isA(Customer.class));
	}

	@Test
	public void testEditCustomer() throws Exception {
		String content = "{\r\n" + 
				"  \"customerId\": 101,\r\n" + 
				"  \"customerName\": \"shalu\",\r\n" + 
				"  \"customerEmail\": \"shalu@gmail.com\",\r\n" + 
				"  \"customerAddress\": \"Kolkata\",\r\n" + 
				"  \"customerPassword\": \"sha\"\r\n" + 
				"}";
		Customer customer = new Customer(101, "shalu", "shalu@gmail.com", "Kolkata", "sha");
		when(customerService.editCustomer(Mockito.isA(Customer.class))).thenReturn(customer);
		mockMvc.perform(put("/v1/customer").content(content).contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(jsonPath("$.customerEmail").exists())
				.andExpect(jsonPath("$.customerName").value("shalu"));
	}

	@Test
	public void testGetCustomer() throws Exception {
		String content = "{\r\n" + 
				"  \"customerId\": 100,\r\n" + 
				"  \"customerName\": \"swathi\",\r\n" + 
				"  \"customerEmail\": \"swathi@gmail.com\",\r\n" + 
				"  \"customerAddress\": \"Hyderabad\",\r\n" + 
				"  \"customerPassword\": \"swa\"\r\n" + 
				"}";
		Customer customer = new Customer(100, "swathi", "swathi@gmail.com", "Hyderabad", "swa");
		when(customerService.getCustomerById(100)).thenReturn(customer);
		mockMvc.perform(get("/v1/customer/100").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(jsonPath("$.customerEmail").exists())
				.andExpect(jsonPath("$.customerName").value("swathi"));
	}

	@Test
	public void testDeleteCustomer() throws Exception {
		Customer customer = new Customer(103, "chaitanya", "chaitanya@gmail.com", "Mumbai", "cha");
		when(customerService.getCustomerById(103)).thenReturn(customer);
		mockMvc.perform(delete("/v1/customer/103").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
	}

}
package com.igorzaitcev.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.service.CustomerManagement;

@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	CustomerManagement management;

	@Test
	void whenFindCustomerInDB_thenReturnCustomer() throws Exception {
		Customer expectedCustomer = new Customer("New", "Address");
		when(management.findCustomer(2L)).thenReturn(Optional.ofNullable(expectedCustomer));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/{id}", 2L)).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedCustomer));
	}

	@Test
	void whenFindCustomerNotInDB_thenReturn404() throws Exception {
		mockMvc.perform(get("/api/v1/customers/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenDeleteCustomerNotInDB_thenReturn404() throws Exception {
		mockMvc.perform(delete("/api/v1/customers/{id}", 2L)).andExpect(status().isNotFound()).andReturn();
	}

	@Test
	void whenDeleteCustomerInDB_thenCallBuisnessModel() throws Exception {
		Customer expectedCustomer = new Customer("New", "Address");
		when(management.findCustomer(2L)).thenReturn(Optional.ofNullable(expectedCustomer));
		mockMvc.perform(delete("/api/v1/customers/{id}", 2L)).andExpect(status().isOk());
		verify(management, times(1)).findCustomer(2L);
		verify(management, times(1)).deleteCustomer(2L);

	}

	@Test
	void whenFindCustomers_thenReturnListCustomer() throws Exception {
		Customer expectedCustomer = new Customer("New", "Address");
		when(management.findCustomers()).thenReturn(List.of(expectedCustomer));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/customers/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(expectedCustomer)));
	}

	@Test
	void whenValidPostInput_thenReturns200() throws Exception {
		Customer customer = new Customer("New", "Address");
		mockMvc.perform(post("/api/v1/customers/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))).andExpect(status().isOk());
	}

	@Test
	void whenValidPostInput_thenMapsToBuisnessModel() throws Exception {
		Customer customer = new Customer("New", "Address");
		mockMvc.perform(post("/api/v1/customers/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))).andExpect(status().isOk());

		ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
		verify(management, times(1)).addCustomer(customerCaptor.capture());
		assertThat(customerCaptor.getValue().getId()).isNull();
		assertThat(customerCaptor.getValue().getName()).isEqualTo("New");
		assertThat(customerCaptor.getValue().getAddress()).isEqualTo("Address");
	}

	@Test
	void whenNotValidPostInput_thenReturns400() throws Exception {
		Customer customer = new Customer(null, "Address");
		mockMvc.perform(post("/api/v1/customers/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(customer))).andExpect(status().isBadRequest());
	}

}

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
import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.service.VendorManagement;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = VendorController.class)
class VendorControllerTest {

	private static final Vendor expectedVendor = new Vendor("New", "Address");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	VendorManagement management;

	@Test
	void whenFindVendorInDB_thenReturnVendor() throws Exception {
		when(management.findVendor(2L)).thenReturn(Optional.ofNullable(expectedVendor));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/vendors/{id}", 2L)).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedVendor));
	}

	@Test
	void whenFindVendorNotInDB_thenReturn404() throws Exception {
		mockMvc.perform(get("/api/v1/vendors/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenDeleteVendorNotInDB_thenReturn404() throws Exception {
		mockMvc.perform(delete("/api/v1/vendors/{id}", 2L)).andExpect(status().isNotFound()).andReturn();
	}

	@Test
	void whenDeleteVendorInDB_thenCallBuisnessModel() throws Exception {
		when(management.findVendor(2L)).thenReturn(Optional.ofNullable(expectedVendor));
		mockMvc.perform(delete("/api/v1/vendors/{id}", 2L)).andExpect(status().isOk());
		verify(management, times(1)).findVendor(2L);
		verify(management, times(1)).deleteVendor(2L);

	}

	@Test
	void whenFindVendors_thenReturnListVendor() throws Exception {
		when(management.findVendors()).thenReturn(List.of(expectedVendor));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/vendors/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(expectedVendor)));
	}

	@Test
	void whenValidPostInput_thenReturns200() throws Exception {
		mockMvc.perform(post("/api/v1/vendors/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(expectedVendor))).andExpect(status().isOk());
	}

	@Test
	void whenValidPostInput_thenMapsToBuisnessModel() throws Exception {
		mockMvc.perform(post("/api/v1/vendors/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(expectedVendor))).andExpect(status().isOk());

		ArgumentCaptor<Vendor> vendorCaptor = ArgumentCaptor.forClass(Vendor.class);
		verify(management, times(1)).addVendor(vendorCaptor.capture());
		assertThat(vendorCaptor.getValue().getId()).isNull();
		assertThat(vendorCaptor.getValue().getName()).isEqualTo("New");
		assertThat(vendorCaptor.getValue().getAddress()).isEqualTo("Address");
	}

	@Test
	void whenNotValidPostInput_thenReturns400() throws Exception {
		Vendor vendor = new Vendor(null, "Address");
		mockMvc.perform(post("/api/v1/vendors/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(vendor))).andExpect(status().isBadRequest());
	}

}

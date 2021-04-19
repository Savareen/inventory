package com.igorzaitcev.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorzaitcev.inventory.dto.DeliveryDTO;
import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.DeliveryManagement;

@ActiveProfiles("test")
@WebMvcTest(controllers = DeliveryController.class)
@AutoConfigureMockMvc(addFilters = false)
class DeliveryControllerTest {

	private static final DeliveryDTO expectedDeliveryDTO = new DeliveryDTO(2L, false, LocalDate.parse("2021-04-14"),
			LocalDate.parse("2021-04-16"), "New", List.of(new ItemDTO("Product", 20)));

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	DeliveryManagement management;

	@Test
	void whenFindDeliveryInDB_thenReturnDelivery() throws Exception {
		when(management.findDelivery(2L)).thenReturn(expectedDeliveryDTO);
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/deliveries/{id}", 2L)).andExpect(status().isOk())
				.andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedDeliveryDTO));
	}

	@Test
	void whenFindDeliveryNotInDB_thenReturn404() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).findDelivery(2L);
		mockMvc.perform(get("/api/v1/deliveries/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenFindDeliveries_thenReturnListDelivery() throws Exception {
		when(management.findActiveDeliveries()).thenReturn(List.of(expectedDeliveryDTO));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/deliveries/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(expectedDeliveryDTO)));
	}

	@Test
	void whenDeliveryProductEnoughInStock_thenCallBuisnessModel() throws Exception {
		mockMvc.perform(put("/api/v1/deliveries/{id}", 2L)).andExpect(status().isOk());
		verify(management, times(1)).validateDelivery(2L);
	}

	@Test
	void whenDeliveryProductNotEnoughInStock_thenCallBuisnessModel() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).validateDelivery(2L);
		mockMvc.perform(put("/api/v1/deliveries/{id}", 2L)).andExpect(status().isBadRequest());
		verify(management, times(1)).validateDelivery(2L);
	}
}

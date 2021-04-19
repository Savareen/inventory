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
import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.ReceiptDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.ReceiptManagement;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ReceiptController.class)
class ReceiptControllerTest {

	private static final ReceiptDTO expectedReceiptDTO = new ReceiptDTO(2L, false, LocalDate.parse("2021-04-14"),
			LocalDate.parse("2021-04-16"), "New", List.of(new ItemDTO("Product", 20)));

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	ReceiptManagement management;

	@Test
	void whenFindReceiptInDB_thenReturnReceipt() throws Exception {
		when(management.findReceipt(2L)).thenReturn(expectedReceiptDTO);
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/receipts/{id}", 2L)).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedReceiptDTO));
	}

	@Test
	void whenFindReceiptNotInDB_thenReturn404() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).findReceipt(2L);
		mockMvc.perform(get("/api/v1/receipts/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenFindReceipts_thenReturnListReceipt() throws Exception {
		when(management.findActiveReceipts()).thenReturn(List.of(expectedReceiptDTO));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/receipts/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(expectedReceiptDTO)));
	}

	@Test
	void whenReceiptValidate_thenCallBuisnessModel() throws Exception {
		mockMvc.perform(put("/api/v1/receipts/{id}", 2L)).andExpect(status().isOk());
		verify(management, times(1)).validateReceipt(2L);
	}

}

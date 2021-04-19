package com.igorzaitcev.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import com.igorzaitcev.inventory.dto.StockRequestDTO;
import com.igorzaitcev.inventory.dto.StockResponseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.service.StockManagement;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = StockController.class)
class StockControllerTest {

	private static final StockRequestDTO requestDTO = new StockRequestDTO(2L, "Product", UnitOfMeasure.LITER, 10, 20,
			10);
	private static final StockResponseDTO responseDTO = new StockResponseDTO(2L, 20);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	StockManagement management;

	@Test
	void whenFindStockInDB_thenReturnStock() throws Exception {
		when(management.findStock(2L)).thenReturn(requestDTO);
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/stocks/{id}", 2L)).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(requestDTO));
	}

	@Test
	void whenFindStockNotInDB_thenReturn404() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).findStock(2L);
		mockMvc.perform(get("/api/v1/stocks/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenFindStocks_thenReturnListStock() throws Exception {
		when(management.findStocks()).thenReturn(List.of(requestDTO));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/stocks/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(requestDTO)));
	}

	@Test
	void whenValidPutInput_thenReturns200() throws Exception {
		mockMvc.perform(put("/api/v1/stocks/initialize").contentType("application/json")
				.content(objectMapper.writeValueAsString(responseDTO))).andExpect(status().isOk());
	}

	@Test
	void whenValidPutInput_thenMapsToBuisnessModel() throws Exception {
		mockMvc.perform(put("/api/v1/stocks/initialize").contentType("application/json")
				.content(objectMapper.writeValueAsString(responseDTO))).andExpect(status().isOk());

		ArgumentCaptor<StockResponseDTO> responseDTOCaptor = ArgumentCaptor.forClass(StockResponseDTO.class);
		verify(management, times(1)).setInitialStock(responseDTOCaptor.capture());
		assertThat(responseDTOCaptor.getValue().getStockId()).isEqualTo(2L);
		assertThat(responseDTOCaptor.getValue().getOnHand()).isEqualTo(20);
	}

	@Test
	void whenNotValidPutInput_thenReturns400() throws Exception {
		StockResponseDTO stockDTO = new StockResponseDTO(2L, null);
		mockMvc.perform(put("/api/v1/stocks/initialize").contentType("application/json")
				.content(objectMapper.writeValueAsString(stockDTO))).andExpect(status().isBadRequest());
	}

}

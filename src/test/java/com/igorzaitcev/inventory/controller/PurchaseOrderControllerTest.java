package com.igorzaitcev.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.PurchaseOrderManagement;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = PurchaseOrderController.class)
class PurchaseOrderControllerTest {

	private static final PurchaseDTO purchaseDTO = new PurchaseDTO(1001, LocalDate.parse("2021-04-14"),
			LocalDate.parse("2021-04-16"), "New", null, List.of(new ItemDTO("Product", 20)));

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	PurchaseOrderManagement management;

	@Test
	void whenFindPurcheInDB_thenReturnPurchase() throws Exception {
		when(management.findPurchaseOrder(2L)).thenReturn(purchaseDTO);
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/purchases/{id}", 2L)).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(purchaseDTO));
	}

	@Test
	void whenFindPurchaseNotInDB_thenReturn404() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).findPurchaseOrder(2L);
		mockMvc.perform(get("/api/v1/purchases/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenDeletePurchaseNotDeleted_thenReturn400() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).deletePurchaseOrder(2L);
		mockMvc.perform(delete("/api/v1/purchases/{id}", 2L)).andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	void whenDeletePurchase_thenCallBuisnessModel() throws Exception {
		mockMvc.perform(delete("/api/v1/purchases/{id}", 2L)).andExpect(status().isOk());
		verify(management, times(1)).deletePurchaseOrder(2L);

	}

	@Test
	void whenFindPurchase_thenReturnListPurchase() throws Exception {
		when(management.findPurchaseOrders()).thenReturn(List.of(purchaseDTO));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/purchases/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(purchaseDTO)));
	}

	@Test
	void whenValidPostInput_thenReturns200() throws Exception {
		mockMvc.perform(post("/api/v1/purchases/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(purchaseDTO))).andExpect(status().isOk());
	}

	@Test
	void whenValidPostInput_thenMapsToBuisnessModel() throws Exception {
		mockMvc.perform(post("/api/v1/purchases/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(purchaseDTO))).andExpect(status().isOk());

		ArgumentCaptor<PurchaseDTO> purchaseDTOCaptor = ArgumentCaptor.forClass(PurchaseDTO.class);
		verify(management, times(1)).addPurchaseOrder(purchaseDTOCaptor.capture());
		assertThat(purchaseDTOCaptor.getValue().getOrderId()).isNull();
		assertThat(purchaseDTOCaptor.getValue().getVendorName()).isEqualTo("New");
		assertThat(purchaseDTOCaptor.getValue().getOrderNumber()).isEqualTo(1001);
	}

	@Test
	void whenNotValidPostInput_thenReturns400() throws Exception {
		PurchaseDTO purchase = new PurchaseDTO(null, LocalDate.parse("2021-04-14"), LocalDate.parse("2021-04-16"),
				"New", null, List.of(new ItemDTO("Product", 20)));
		mockMvc.perform(post("/api/v1/purchases/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(purchase))).andExpect(status().isBadRequest());
	}
}

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
import com.igorzaitcev.inventory.dto.ProductDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.service.ProductManagement;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

	private static final ProductDTO expectedProductDTO = new ProductDTO(2L, "Product", UnitOfMeasure.LITER, 10, 20);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	ProductManagement management;

	@Test
	void whenFindProductInDB_thenReturnProduct() throws Exception {
		when(management.findProduct(2L)).thenReturn(expectedProductDTO);
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/{id}", 2L)).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedProductDTO));
	}

	@Test
	void whenFindProductNotInDB_thenReturn404() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).findProduct(2L);
		mockMvc.perform(get("/api/v1/products/{id}", 2L)).andExpect(status().isNotFound());
	}

	@Test
	void whenDeleteProductNotDeleted_thenReturn400() throws Exception {
		doThrow(InvalidArgumentException.class).when(management).deleteProduct(2L);
		mockMvc.perform(delete("/api/v1/products/{id}", 2L)).andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	void whenDeleteProduct_thenCallBuisnessModel() throws Exception {
		mockMvc.perform(delete("/api/v1/products/{id}", 2L)).andExpect(status().isOk());
		verify(management, times(1)).deleteProduct(2L);

	}

	@Test
	void whenFindProducts_thenReturnListProduct() throws Exception {
		when(management.findProducts()).thenReturn(List.of(expectedProductDTO));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/all")).andExpect(status().isOk()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(List.of(expectedProductDTO)));
	}

	@Test
	void whenValidPostInput_thenReturns200() throws Exception {
		mockMvc.perform(post("/api/v1/products/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(expectedProductDTO))).andExpect(status().isOk());
	}

	@Test
	void whenValidPostInput_thenMapsToBuisnessModel() throws Exception {
		mockMvc.perform(post("/api/v1/products/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(expectedProductDTO))).andExpect(status().isOk());

		ArgumentCaptor<ProductDTO> productDTOCaptor = ArgumentCaptor.forClass(ProductDTO.class);
		verify(management, times(1)).addProduct(productDTOCaptor.capture());
		assertThat(productDTOCaptor.getValue().getProductId()).isEqualTo(2L);
		assertThat(productDTOCaptor.getValue().getName()).isEqualTo("Product");
		assertThat(productDTOCaptor.getValue().getUnits()).isEqualTo(UnitOfMeasure.LITER);
	}

	@Test
	void whenNotValidPostInput_thenReturns400() throws Exception {
		ProductDTO productDTO = new ProductDTO(2L, "Product", UnitOfMeasure.LITER, null, 20);
		mockMvc.perform(post("/api/v1/products/add").contentType("application/json")
				.content(objectMapper.writeValueAsString(productDTO))).andExpect(status().isBadRequest());
	}
}

package com.htwberlin.productservice.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;
import com.htwberlin.productservice.core.domain.service.impl.ProductService;
import com.htwberlin.productservice.user.advice.ProductNotFoundAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController controller;

    private MockMvc mockMvc;

    final List<Product> products = new LinkedList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(ProductNotFoundAdvice.class)
                .build();
        Category[] categories = Category.values();

        // 20 dummy products
        for (int i = 0; i < 20; i++) {
            int random = new Random().nextInt(Category.values().length);
            Product product = Product.builder()
                    .id(UUID.randomUUID())
                    .name("Product " + i)
                    .category(categories[random])
                    .description("Very cool product")
                    .imageLink("http://imageLink.com")
                    .price(new BigDecimal(250 + i))
                    .build();

            products.add(product);
        }
    }

    @Test
    void getAllProductsTest() throws Exception {

        // setup
        when(productService.getAllProducts()).thenReturn(products);

        // exercise
        MvcResult res = mockMvc.perform(get("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = res.getResponse().getContentAsString();
        List<Product> resProducts = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        // verify
        assertEquals(products, resProducts);
    }

    @Test
    void getProductTest() throws Exception {
        // setup
        UUID randomUuid = UUID.randomUUID();
        when(productService.getProduct(any(UUID.class))).thenThrow(new ProductNotFoundException(randomUuid));

        // exercise and verify
        mockMvc.perform(get("/v1/product/{id}", randomUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find product " + randomUuid))
                .andReturn();
    }
}

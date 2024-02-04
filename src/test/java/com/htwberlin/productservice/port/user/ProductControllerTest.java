package com.htwberlin.productservice.port.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.exception.ProductNotFoundException;
import com.htwberlin.productservice.core.domain.service.impl.ProductService;
import com.htwberlin.productservice.port.user.advice.ProductNotFoundAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

        // 20 random products
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
        when(productService.getAllProducts()).thenReturn(products);
        String productsJson = objectMapper.writeValueAsString(products);

        MvcResult res = mockMvc.perform(get("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = res.getResponse().getContentAsString();

        assertEquals(productsJson, responseBody);
    }

    @Test
    void getProductNotFoundTest() throws Exception {
        UUID randomUuid = UUID.randomUUID();
        when(productService.getProduct(any(UUID.class))).thenThrow(new ProductNotFoundException(randomUuid));

        mockMvc.perform(get("/v1/product/{id}", randomUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find product " + randomUuid))
                .andReturn();
    }


    @Test
    void getProductFoundTest() throws Exception {
        Product product = products.get(new Random().nextInt(products.size()));
        String productJson = objectMapper.writeValueAsString(product);
        when(productService.getProduct(eq(product.getId()))).thenReturn(product);

        MvcResult result = mockMvc.perform(get("/v1/product/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(productJson, content);
    }

    @Test
    void postProductTest() throws Exception {

        Product product = products.get(new Random().nextInt(products.size()));
        String productJson = objectMapper.writeValueAsString(product);
        when(productService.createProduct(product)).thenReturn(product);

        MvcResult result = mockMvc.perform(post("/v1/product")
                        .content(productJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(productJson, content);
    }


    @Test
    void postProductBadRequestTest() throws Exception {
        mockMvc.perform(post("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void putBadRequestTest() throws Exception {
        mockMvc.perform(put("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void putProductTest() throws Exception {
        Product product = products.get(products.size() / 2);
        String productJson = objectMapper.writeValueAsString(product);
        when(productService.updateProduct(any(Product.class))).thenReturn(product);

        MvcResult result = mockMvc.perform(put("/v1/product")
                        .content(productJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertEquals(productJson, responseContent);
    }

    @Test
    void putProductNotFoundTest() throws Exception {
        Product product = products.get(0);
        String productJson = objectMapper.writeValueAsString(product);
        String notFoundResponse = "Could not find product " + product.getId().toString();

        when(productService.updateProduct(any(Product.class))).thenThrow(new ProductNotFoundException(product.getId()));

        MvcResult result = mockMvc.perform(put("/v1/product")
                        .content(productJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertEquals(notFoundResponse, responseContent);
    }

    @Test
    void getByKeywordTest() throws Exception {
        String keyword = "Product 1";
        List<Product> expectedProducts = products.stream()
                .filter(product -> product.getName().contains(keyword))
                .collect(Collectors.toList());
        when(productService.getProductsByKeyword(keyword)).thenReturn(expectedProducts);

        String expectedJson = objectMapper.writeValueAsString(expectedProducts);

        MvcResult result = mockMvc.perform(get("/v1/product/search")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertEquals(expectedJson, responseContent);
    }

    @Test
    void getByCategoryTest() throws Exception {
        String category = "videocard";
        Category expectedCategory = Category.VIDEOCARD;

        List<Product> expectedProducts = products.stream()
                .filter(product -> product.getCategory().equals(expectedCategory))
                .collect(Collectors.toList());

        when(productService.getProductsByCategory(Category.valueOf(category.toUpperCase()))).thenReturn(expectedProducts);

        String expectedJson = objectMapper.writeValueAsString(expectedProducts);

        MvcResult result = mockMvc.perform(get("/v1/products/filter")
                        .param("category", category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertEquals(expectedJson, responseContent);
    }

    @Test
    void deleteProductTest() throws Exception {
        Product product = products.get(0);

        mockMvc.perform(delete("/v1/product/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(productService, times(1)).deleteProduct(product.getId());
    }
}

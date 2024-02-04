package com.htwberlin.productservice.port.user;

import com.htwberlin.productservice.core.domain.model.Category;
import com.htwberlin.productservice.core.domain.model.Product;
import com.htwberlin.productservice.core.domain.service.interfaces.IProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
@Slf4j
public class ControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:12");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7")
            .withExposedPorts(6379);

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        redis.start();
    }

    @AfterAll
    static void afterAll() {
        redis.stop();
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void afterEach() {
        productRepository.deleteAll();
        cacheManager.getCacheNames()
                .stream()
                .map(name -> cacheManager.getCache(name))
                .filter(Objects::nonNull)
                .forEach(Cache::clear);
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();
        assertEquals(10, products.size());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/products")
                .then()
                .statusCode(200)
                .body(".", hasSize(10));
    }

    @Test
    void shouldGetOnlyVideocards() {
        String category = "videocard";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/products/filter?category=" + category)
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    void shouldGetOnlyPeripherals() {
        String category = "peripheral";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/products/filter?category=" + category)
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void requestWrongCategory() {
        String wrongCategory = "somethingWrong";

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/products/filter?category=" + wrongCategory)
                .then()
                .statusCode(400);
    }

    @Test
    void addProductToDatabase() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("50"))
                .imageLink("https://link.com")
                .category(Category.VIDEOCARD)
                .name("New Product")
                .description("Some cool new product")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(product)
                .post("/v1/product")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .get("/v1/products")
                .then()
                .statusCode(200)
                .body(containsString(product.getName()))
                .body(".", hasSize(11));

        given()
                .contentType(ContentType.JSON)
                .delete("/v1/product/" + product.getId())
                .then()
                .statusCode(200);

        assertFalse(productRepository.existsById(product.getId()));
    }
}

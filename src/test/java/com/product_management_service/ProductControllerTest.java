package com.product_management_service;

import com.product_management_service.controller.ProductController;
import com.product_management_service.dao.ProductDAO;
import com.product_management_service.dto.CreateProductDTO;
import com.product_management_service.dto.ProductAttributeDTO;
import com.product_management_service.dto.ProductImageDTO;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.entity.ProductImage;
import com.product_management_service.exceptions.ErrorResponse;
import com.product_management_service.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashMap;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
public class ProductControllerTest {

    private static final String PATH_URL = "/products/";
    @MockBean
    private ProductService productService;

    @Autowired
    private WebTestClient webTestClient;

    public CreateProductDTO createProductDTO(){
        return  CreateProductDTO.builder()
                .name("Iphone")
                .brand("Apple")
                .description("it is a phone")
                .height(10.1F)
                .length(10F)
                .width(10F)
                .productImages(Arrays.asList(ProductImageDTO.builder()
                        .imageType("Lifestyle")
                        .imageUrl("https://www.iphone.com")
                                .build())
                ).productAttributes(Arrays.asList(ProductAttributeDTO.builder()
                        .name("size")
                        .value("medium")
                        .build()))
                .build();
    }

    public Product createProduct(){
        return  Product.builder()
                .productId("76767868706767")
                .name("Iphone")
                .brand("Apple")
                .description("it is a phone")
                .height(10.1F)
                .length(10F)
                .width(10F)
                .productImages(Arrays.asList(ProductImage.builder()
                        .imageType("Lifestyle")
                        .imageUrl("https://www.iphone.com")
                        .build())
                ).productAttributes(Arrays.asList(ProductAttribute.builder()
                        .name("size")
                        .value("medium")
                        .build()))
                .build();
    }

    @Test
    public void when_create_product_with_null_values_then_return_exception(){
        CreateProductDTO createProductDTO = new CreateProductDTO();

        webTestClient.post()
                .uri(PATH_URL+"createProduct")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createProductDTO),CreateProductDTO.class).exchange()
                .expectStatus().isBadRequest()
                .expectBody(Object.class).value(errorResponse -> {
                    System.out.println(errorResponse);
                });
    }

    @Test
    public void when_create_product_then_return_ok_status() {
        CreateProductDTO createProductDTO = createProductDTO();
        Mono<CreateProductDTO> createProductDTOMono = Mono.just(createProductDTO);
        Product product = createProduct();
        Mono<Product> productMono = Mono.just(product);

        Mockito.when(productService.createProduct(createProductDTO)).thenReturn(productMono);

        webTestClient.post()
                .uri(PATH_URL + "createProduct")
                .contentType(MediaType.APPLICATION_JSON)
                .body(createProductDTOMono, CreateProductDTO.class)
                .exchange()
                .expectStatus().isOk();
    }
//    @Test
//    public void when_create_product_then_return_product() {
//        CreateProductDTO createProductDTO = createProductDTO();
//
//        Mono<CreateProductDTO> createProductDTOMono = Mono.just(createProductDTO);
//
//        Product product = createProduct();
//
//        Mono<Product> productMono = Mono.just(product);
//
//
//        Mockito.when(productService.createProduct(createProductDTO)).thenReturn(productMono);
//
//        webTestClient.post()
//                .uri(PATH_URL + "createProduct")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(createProductDTOMono,CreateProductDTO.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Product.class).value((p)->{
//                    System.out.println(p);
//                });
//    }

    @Test
    public void when_get_products_then_return_ok_status(){
        Product product1 = createProduct();
        Product product2 = createProduct();

      Mockito.when(productService.getAllProducts()).thenReturn(Flux.just(product1,product2));

         webTestClient.get()
                .uri(PATH_URL+"all")
                .exchange()
                .expectStatus().isOk();

    }


}

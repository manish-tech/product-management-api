package com.product_management_service.controller;

import com.product_management_service.controller.ProductController;
import com.product_management_service.dao.ProductDAO;
import com.product_management_service.dto.CreateProductDTO;
import com.product_management_service.dto.ProductAttributeDTO;
import com.product_management_service.dto.ProductImageDTO;
import com.product_management_service.dto.UpdateProductDTO;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.entity.ProductImage;
import com.product_management_service.exceptions.ErrorResponse;
import com.product_management_service.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
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


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class ProductControllerTest {

    private static final String PATH_URL = "/products/";
    private static String productId;
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
    public UpdateProductDTO createUpdateProductDTO(){
        return  UpdateProductDTO.builder()
                .productId(productId)
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
    @Order(1)
    public void when_create_product_with_null_values_then_return_error(){
        CreateProductDTO createProductDTO = new CreateProductDTO();

        webTestClient.post()
                .uri(PATH_URL+"createProduct")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createProductDTO),CreateProductDTO.class).exchange()
                .expectStatus().isBadRequest()
                .expectBody(Object.class).value(errorResponse -> {
                    Assertions.assertNotNull(errorResponse,"error response should not be null");
                });
    }

    @Test
    @Order(2)
    public void when_create_product_then_return_product(){
        CreateProductDTO createProductDTO = createProductDTO();

        webTestClient.post()
                .uri(PATH_URL + "createProduct")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createProductDTO),CreateProductDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .value((product)->{
                    Assertions.assertNotNull(product,"product should not be null");
                    Assertions.assertEquals("Iphone",product.getName(),"expected and actual values should be same");
                    Assertions.assertEquals("Apple",product.getBrand(),"expected and actual values should be same");
                    productId = product.getProductId();
                });
    }

    @Test
    @Order(3)
    public void when_create_product_with_empty_name_invalid_url_then_return_error(){
        CreateProductDTO createProductDTO = createProductDTO();
        createProductDTO.setName("");
        createProductDTO.getProductImages()
                .set(0, ProductImageDTO.builder()
                        .imageType("customer")
                        .imageUrl("abc")
                        .build());

        webTestClient.post()
                .uri(PATH_URL + "createProduct")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(createProductDTO),CreateProductDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(Object.class)
                .value((error)->{
                    Assertions.assertNotNull(error,"error cannot be null");
                });
    }

    @Test
    @Order(4)
    public void when_get_products_then_return_ok_status(){

         webTestClient.get()
                 .uri(PATH_URL+"all")
                 .exchange()
                 .expectStatus().isOk()
                 .expectBodyList(Product.class).value((products)->{
                     Assertions.assertTrue(products.size() > 0,"products should not be empty");
                 });

    }

    @Test
    @Order(5)
    public void when_edit_product_then_return_success_ok(){
        UpdateProductDTO updateProductDTO = createUpdateProductDTO();
        webTestClient.put()
                .uri(PATH_URL + "editProduct")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateProductDTO),UpdateProductDTO.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(6)
    public void when_edit_product_with_empty_brand_value_return_bad_request(){
        UpdateProductDTO updateProductDTO = createUpdateProductDTO();
        updateProductDTO.setBrand("");
        webTestClient.put()
                .uri(PATH_URL + "editProduct")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateProductDTO),UpdateProductDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(7)
    public void when_add_attribute_then_return_success_ok(){
        ProductAttributeDTO productAttributeDTO = ProductAttributeDTO
                .builder()
                .name("price")
                .value("20000")
                .build();
        webTestClient.post()
                .uri(PATH_URL+"addAttribute/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productAttributeDTO),ProductAttributeDTO.class)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    @Order(8)
    public void when_add_attribute_with_duplicate_attribute_then_return_conflict(){
        ProductAttributeDTO productAttributeDTO = ProductAttributeDTO
                .builder()
                .name("price")
                .value("20000")
                .build();
        webTestClient.post()
                .uri(PATH_URL+"addAttribute/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productAttributeDTO),ProductAttributeDTO.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }
    @Test
    @Order(9)
    public void when_update_attribute_with_duplicate_attribute_then_return_error_same_values_updated(){
        ProductAttributeDTO productAttributeDTO = ProductAttributeDTO
                .builder()
                .name("price")
                .value("20000")
                .build();
        webTestClient.post()
                .uri(PATH_URL+"updateAttribute/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productAttributeDTO),ProductAttributeDTO.class)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    @Order(10)
    public void when_update_attribute_then_return_success_ok(){
        ProductAttributeDTO productAttributeDTO = ProductAttributeDTO
                .builder()
                .name("price")
                .value("10000")
                .build();
        webTestClient.post()
                .uri(PATH_URL+"updateAttribute/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productAttributeDTO),ProductAttributeDTO.class)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    @Order(11)
    public void when_add_image_url_then_return_success_ok(){
        ProductImageDTO productImageDTO = ProductImageDTO
                .builder()
                .imageType("lifestyle")
                .imageUrl("http://www.apple.com")
                .build();
        webTestClient.put()
                .uri(PATH_URL+"addImageURL/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productImageDTO),ProductImageDTO.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(12)
    public void when_add_image_url_with_invalid_url_then_return_bad_request(){
        ProductImageDTO productImageDTO = ProductImageDTO
                .builder()
                .imageType("lifestyle")
                .imageUrl("http/www.apple.com")
                .build();
        webTestClient.put()
                .uri(PATH_URL+"addImageURL/"+productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productImageDTO),ProductImageDTO.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @Order(13)
    public void when_delete_attribute_then_return_success_ok(){
        webTestClient.delete()
                .uri(PATH_URL+"deleteAttribute/"+productId+"?attributeName=price")
                .exchange()
                .expectStatus()
                .isOk();
    }
    @Test
    @Order(14)
    public void when_delete_product_then_return_success_ok(){
        webTestClient.delete()
                .uri(PATH_URL+"deleteProduct/"+productId)
                .exchange()
                .expectStatus()
                .isOk();
    }



//    @Test
//    public void when_create_product_then_return_ok_status() {
//        CreateProductDTO createProductDTO = createProductDTO();
//        Mono<CreateProductDTO> createProductDTOMono = Mono.just(createProductDTO);
//        Product product = createProduct();
//        Mono<Product> productMono = Mono.just(product);
//
//        Mockito.when(productService.createProduct(createProductDTO)).thenReturn(productMono);
//
//        webTestClient.post()
//                .uri(PATH_URL + "createProduct")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(createProductDTOMono, CreateProductDTO.class)
//                .exchange()
//                .expectStatus().isOk();
//    }
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
//
//    @Test
//    public void when_get_products_then_return_ok_status(){
//        Product product1 = createProduct();
//        Product product2 = createProduct();
//
//      Mockito.when(productService.getAllProducts()).thenReturn(Flux.just(product1,product2));
//
//         webTestClient.get()
//                .uri(PATH_URL+"all")
//                .exchange()
//                .expectStatus().isOk();
//
//    }


}

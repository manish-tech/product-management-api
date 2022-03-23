package com.product_management_service.controller;

import com.product_management_service.dto.*;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.exceptions.ErrorCode;
import com.product_management_service.exceptions.SystemException;
import com.product_management_service.utills.AttributeUtills;
import com.product_management_service.utills.ProductImageUtills;
import com.product_management_service.utills.Validation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.product_management_service.service.ProductService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

   @GetMapping("/all")
    public Flux<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("createProduct")
    public Mono<Product> createProduct(@Valid @RequestBody CreateProductDTO createProductDTO){
        SystemException exception = null;
        if((exception = Validation.duplicateProductAttributeNameAndImages(createProductDTO)) != null){
           return Mono.error(exception);
       }
       return productService.createProduct(createProductDTO);
    }
    @PutMapping("editProduct")
    public Mono<UpdateResponseDTO> editProduct(@Valid @RequestBody UpdateProductDTO updateProductDTO){
        SystemException exception = null;
        if((exception = Validation.duplicateProductAttributeNameAndImages(updateProductDTO)) != null){
            return Mono.error(exception);
        }
       return productService.editProduct(updateProductDTO);
    }
    @PostMapping("addAttribute/{productId}")
    public Mono<UpdateResponseDTO> addAttribute(@Valid @RequestBody ProductAttributeDTO productAttributeDTO,@PathVariable String productId){
       return productService.addAttribute(productAttributeDTO,productId);
    }
    @PutMapping("updateAttribute/{productId}")
    public Mono<UpdateResponseDTO> updateAttribute(@Valid @RequestBody ProductAttributeDTO productAttributeDTO,@PathVariable String productId , @RequestParam String currentName ){
        return productService.updateAttribute(productAttributeDTO,productId,currentName);
    }

    @PutMapping("addImageURL/{productId}")
    public Mono<UpdateResponseDTO> addImageUrl(@Valid @RequestBody ProductImageDTO productImageDTO,@PathVariable String productId){
       return productService.addImageUrl(productImageDTO,productId);
    }

    @DeleteMapping("deleteAttribute/{productId}")
    public Mono<UpdateResponseDTO> deleteAttribute(@PathVariable String productId, @RequestParam String attributeName){
       return productService.deleteAttribute(productId,attributeName);
    }
    @DeleteMapping("deleteProduct/{productId}")
    public Mono<UpdateResponseDTO> deleteProduct(@PathVariable String productId){
        return productService.deleteProduct(productId);
    }
}

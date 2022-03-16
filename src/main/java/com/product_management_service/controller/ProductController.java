package com.product_management_service.controller;

import com.product_management_service.dto.*;
import com.product_management_service.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.product_management_service.service.ProductService;

import javax.validation.Valid;

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
       return productService.createProduct(createProductDTO);
    }
    @PutMapping("editProduct")
    public Mono<UpdateResponseDTO> editProduct(@Valid @RequestBody UpdateProductDTO updateProductDTO){
       return productService.editProduct(updateProductDTO);
    }
    @PostMapping("addAttribute/{productId}")
    public Mono<UpdateResponseDTO> addAttribute(@RequestBody ProductAttributeDTO productAttributeDTO,@PathVariable String productId){
       return productService.addAttribute(productAttributeDTO,productId);
    }
    @PutMapping("updateAttribute/{productId}")
    public Mono<UpdateResponseDTO> updateAttribute(@RequestBody ProductAttributeDTO productAttributeDTO,@PathVariable String productId , @RequestParam String currentName ){
        return productService.updateAttribute(productAttributeDTO,productId,currentName);
    }

    @PutMapping("addImageURL/{productId}")
    public Mono<UpdateResponseDTO> addImageUrl(@RequestBody ProductImageDTO productImageDTO,@PathVariable String productId){
       return productService.addImageUrl(productImageDTO,productId);
    }

    @DeleteMapping("deleteAttribute/{productId}")
    public Mono<UpdateResponseDTO> deleteAttribute(@PathVariable String productId,@RequestParam String attributeName){
       return productService.deleteAttribute(productId,attributeName);
    }
    @DeleteMapping("deleteProduct/{productId}")
    public Mono<UpdateResponseDTO> deleteProduct(@PathVariable String productId){
        return productService.deleteProduct(productId);
    }
}

package com.product_management_service.service;

import com.product_management_service.dao.ProductDAO;
import com.product_management_service.dto.*;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.entity.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.product_management_service.utills.ProductUtills;

@Service
public class ProductService {
    @Autowired
    private ProductDAO productDAO;

    public Mono<Product> createProduct(CreateProductDTO createProductDTO){
        Product product = ProductUtills.dtoToEntity(createProductDTO);
        return productDAO.saveProduct(product);
    }
    public Mono<UpdateResponseDTO> editProduct(UpdateProductDTO updateProductDTO){
        return productDAO.editProduct(updateProductDTO);
    }

    public Mono<UpdateResponseDTO> deleteProduct(String productId){
        return productDAO.removeProduct(productId);
    }
    public Mono<UpdateResponseDTO> deleteAttribute(String productId,String attributeName){
        return productDAO.deleteAttribute(productId,attributeName);
    }

    public Flux<Product> getAllProducts(){
        return productDAO.getAllProducts();
    }

    public Mono<UpdateResponseDTO> addAttribute(ProductAttributeDTO productAttributeDTO,String productId){
        ProductAttribute productAttribute = ProductUtills.productAttributeDTOToProductAttributes(productAttributeDTO);
        return productDAO.addAttribute(productAttribute,productId);
    }
    public Mono<UpdateResponseDTO> addImageUrl(ProductImageDTO productImageDTO,String productId){
        ProductImage productImage = ProductUtills.productImageDTOProductImage(productImageDTO);
        return productDAO.addImageUrl(productImage,productId);
    }
    public Mono<UpdateResponseDTO> updateAttribute(ProductAttributeDTO productAttributeDTO,String productId,String currentName){
        ProductAttribute productAttribute = ProductUtills.productAttributeDTOToProductAttributes(productAttributeDTO);
        return productDAO.updateAttribute(currentName,productId,productAttribute);
    }

}

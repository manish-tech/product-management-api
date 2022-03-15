package com.product_management_service.service;

import com.product_management_service.dao.ProductDAO;
import com.product_management_service.dto.CreateProductDTO;
import com.product_management_service.dto.ProductAttributeDTO;
import com.product_management_service.dto.ProductImageDTO;
import com.product_management_service.dto.UpdateProductDTO;
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
    public Mono<Product> editProduct(UpdateProductDTO updateProductDTO){
        return productDAO.editProduct(updateProductDTO);
    }

    public Mono<Void> deleteProduct(String productId){
        return productDAO.removeProduct(productId);
    }
    public Mono<Long> deleteAttribute(String productId,String attributeName){
        return productDAO.deleteAttribute(productId,attributeName);
    }

    public Flux<Product> getAllProducts(){
        return productDAO.getAllProducts();
    }

    public Mono<Long> addAttribute(ProductAttributeDTO productAttributeDTO,String productId){
        ProductAttribute productAttribute = ProductUtills.productAttributeDTOToProductAttributes(productAttributeDTO);
        return productDAO.addAttribute(productAttribute,productId);
    }
    public Mono<Long> addImageUrl(ProductImageDTO productImageDTO,String productId){
        ProductImage productImage = ProductUtills.productImageDTOProductImage(productImageDTO);
        return productDAO.addImageUrl(productImage,productId);
    }
    public Mono<Long> updateAttribute(ProductAttributeDTO productAttributeDTO,String productId,String currentName){
        ProductAttribute productAttribute = ProductUtills.productAttributeDTOToProductAttributes(productAttributeDTO);
        return productDAO.updateAttribute(currentName,productId,productAttribute);
    }

}

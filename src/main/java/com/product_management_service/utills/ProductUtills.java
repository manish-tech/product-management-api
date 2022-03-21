package com.product_management_service.utills;

import com.product_management_service.dto.CreateProductDTO;
import com.product_management_service.dto.ProductAttributeDTO;
import com.product_management_service.dto.ProductImageDTO;
import com.product_management_service.dto.UpdateProductDTO;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.entity.ProductImage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductUtills {

    public static Product dtoToEntity(CreateProductDTO createProductDTO){
        Product product = Product.builder()
                .name(createProductDTO.getName())
                .description(createProductDTO.getDescription())
                .brand(createProductDTO.getBrand())
                .length(createProductDTO.getLength())
                .width(createProductDTO.getWidth())
                .height(createProductDTO.getHeight())
                .productAttributes(productAttributeDTOsToProductAttributes(createProductDTO.getProductAttributes()))
                .productImages(productImageDTOsToProductImages(createProductDTO.getProductImages()))
                .build();
        return product;
    }
    public static Product dtoToEntity(UpdateProductDTO updateProductDTO){
        Product product = Product.builder()
                .productId(updateProductDTO.getProductId())
                .name(updateProductDTO.getName())
                .description(updateProductDTO.getDescription())
                .brand(updateProductDTO.getBrand())
                .length(updateProductDTO.getLength())
                .width(updateProductDTO.getWidth())
                .height(updateProductDTO.getHeight())
                .productAttributes(productAttributeDTOsToProductAttributes(updateProductDTO.getProductAttributes()))
                .productImages(productImageDTOsToProductImages(updateProductDTO.getProductImages()))
                .build();
        return product;
    }

    public static List<ProductAttribute> productAttributeDTOsToProductAttributes(List<ProductAttributeDTO> productAttributesDTO){
        return productAttributesDTO
                .stream()
                .map((attribute)->{
                    return productAttributeDTOToProductAttribute(attribute);
                }).collect(Collectors.toList());
    }

    public static List<ProductImage> productImageDTOsToProductImages(List<ProductImageDTO> productImagesDTO){
        return productImagesDTO
                .stream()
                .map((image)->{
                    return productImageDTOProductImage(image);
                }).collect(Collectors.toList());
    }

    public static ProductAttribute productAttributeDTOToProductAttribute(ProductAttributeDTO productAttributesDTO){
        return ProductAttribute.builder()
                .name(productAttributesDTO.getName())
                .value(productAttributesDTO.getValue())
                .build();
    }

    public static ProductImage productImageDTOProductImage(ProductImageDTO productImageDTO){
        return ProductImage.builder()
                .imageType(productImageDTO.getImageType())
                .imageUrl(productImageDTO.getImageUrl())
                .build();
    }

    public static List<String> getFeildNamesOfAClass(String className){
        try {
            Class<?> aClass= Class.forName(className);
            return Arrays.stream(aClass.getDeclaredFields()).map((field)->{
                return field.getName();
            }).collect(Collectors.toList());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

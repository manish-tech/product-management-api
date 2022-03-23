package com.product_management_service.utills;

import com.product_management_service.dto.CreateProductDTO;
import com.product_management_service.dto.UpdateProductDTO;
import com.product_management_service.dto.UpdateResponseDTO;
import com.product_management_service.exceptions.ErrorCode;
import com.product_management_service.exceptions.SystemException;
import reactor.core.publisher.Mono;

public class Validation {
    public static SystemException duplicateProductAttributeNameAndImages(CreateProductDTO createProductDTO){
        Boolean first = Boolean.FALSE;
        Boolean second = Boolean.FALSE;

        if((first = AttributeUtills.checkDuplicateAttributeName(createProductDTO.getProductAttributes())) || (second = ProductImageUtills.checkDuplicateImage(createProductDTO.getProductImages()))){
            if(first && second){
                return SystemException.builder()
                        .errorCode(ErrorCode.PRODUCT_VALIDATION_ERROR)
                        .message("duplicate attribute names and images")
                        .build();

            }
            else if(first){
                return SystemException.builder()
                        .errorCode(ErrorCode.PRODUCT_VALIDATION_ERROR)
                        .message("duplicate attribute names")
                        .build();
            }
            else if(second){
                return SystemException.builder()
                        .errorCode(ErrorCode.PRODUCT_VALIDATION_ERROR)
                        .message("duplicate Images")
                        .build();

            }
        }
        return null;
    }
    public static SystemException duplicateProductAttributeNameAndImages(UpdateProductDTO updateProductDTO){
        Boolean first = Boolean.FALSE;
        Boolean second = Boolean.FALSE;

        if((first = AttributeUtills.checkDuplicateAttributeName(updateProductDTO.getProductAttributes())) || (second = ProductImageUtills.checkDuplicateImage(updateProductDTO.getProductImages()))){
            if(first && second){
                return SystemException.builder()
                        .errorCode(ErrorCode.PRODUCT_VALIDATION_ERROR)
                        .message("duplicate attribute names and images")
                        .build();

            }
            else if(first){
                return SystemException.builder()
                        .errorCode(ErrorCode.PRODUCT_VALIDATION_ERROR)
                        .message("duplicate attribute names")
                        .build();
            }
            else if(second){
                return SystemException.builder()
                        .errorCode(ErrorCode.PRODUCT_VALIDATION_ERROR)
                        .message("duplicate Images")
                        .build();

            }
        }
        return null;
    }
}

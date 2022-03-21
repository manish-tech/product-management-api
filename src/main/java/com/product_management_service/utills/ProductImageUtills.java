package com.product_management_service.utills;

import com.product_management_service.dto.ProductImageDTO;
import com.product_management_service.entity.ProductImage;

import java.util.List;

public class ProductImageUtills {
    public static Boolean checkDuplicateImage(List<ProductImage> productImageList , List<ProductImageDTO> productImageDTOList){
        for (ProductImage productImage : productImageList){
            Integer count = 0;

            for (ProductImageDTO productImageDTO : productImageDTOList){
                if(productImage.getImageUrl().equals(productImageDTO.getImageUrl()) && productImage.getImageType().equals(productImageDTO.getImageType())){
                    count++;
                }
                if(count == 2){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}

package com.product_management_service.utills;

import com.product_management_service.dto.ProductAttributeDTO;
import com.product_management_service.entity.ProductAttribute;

import java.util.List;

public class AttributeUtills {


    public static Boolean checkDuplicateAttributeName(List<ProductAttribute> productAttributeList, List<ProductAttributeDTO> productAttributeDTOList){

        for ( ProductAttribute productAttribute : productAttributeList){
            String name = productAttribute.getName();
            Integer count = 0;
            for ( ProductAttributeDTO productAttributeDTO : productAttributeDTOList){
                if(productAttributeDTO.getName().equals(name)){
                    count++;
                }
                if(count == 2){
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    public static Boolean checkDuplicateAttributeName(List<ProductAttribute> productAttributeList , ProductAttribute productAttribute){
        Integer count = 0;
        for (ProductAttribute productAttribute1 : productAttributeList){
            if (productAttribute.getName().equals(productAttribute1.getName())){
                count++;
            }
            if(count == 2) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}

package com.product_management_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductImageDTO {
    private String imageType;
    private String imageUrl;
}

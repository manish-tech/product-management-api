package com.product_management_service.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ProductImage implements Serializable {
    private String imageType;
    private String imageUrl;
}

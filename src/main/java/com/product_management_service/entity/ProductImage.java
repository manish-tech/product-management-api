package com.product_management_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ProductImage implements Serializable {
    private String imageType;
    private String imageUrl;
}

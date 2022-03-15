package com.product_management_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateProductDTO {
    private String name;
    private String description;
    private String brand;
    private Float length;
    private Float width;
    private Float height;
    private List<ProductAttributeDTO> productAttributes = new ArrayList<>();
    private List<ProductImageDTO> productImages = new ArrayList<>();
}

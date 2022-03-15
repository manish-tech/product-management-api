package com.product_management_service.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDTO {
    private String productId;
    private String name;
    private String description;
    private String brand;
    private Float length;
    private Float width;
    private Float height;
    private List<ProductAttributeDTO> productAttributes = new ArrayList<>();
    private List<ProductImageDTO> productImages = new ArrayList<>();
}

package com.product_management_service.dto;

import com.product_management_service.constants.MessageConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
public class ProductImageDTO {
    @NotEmpty(message = "image type " + MessageConstants.INVALID_MISSING)
    private String imageType;
    @NotEmpty(message = "image URL" + MessageConstants.INVALID_MISSING)
    private String imageUrl;
}

package com.product_management_service.dto;

import com.product_management_service.constants.MessageConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
public class ProductAttributeDTO {
    @NotEmpty(message = "name " + MessageConstants.INVALID_MISSING)
    private String name;
    @NotEmpty(message = "value " + MessageConstants.INVALID_MISSING)
    private String value;
}

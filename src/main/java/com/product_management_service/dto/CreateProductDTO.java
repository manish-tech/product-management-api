package com.product_management_service.dto;

import com.product_management_service.constants.MessageConstants;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {
   @NotEmpty(message = "name " + MessageConstants.INVALID_MISSING)
    private String name;
   @NotEmpty
   @Length(min = 1,max = 256,message = "description "+MessageConstants.VALUE_OVER_FLOW + "256")
    private String description;
   @NotEmpty(message = "brand "+MessageConstants.INVALID_MISSING)
    private String brand;
   @NotNull(message = "length " +MessageConstants.INVALID_MISSING)
    private Float length;
   @NotNull(message = "width "+MessageConstants.INVALID_MISSING)
    private Float width;
   @NotNull(message = "height" + MessageConstants.INVALID_MISSING)
    private Float height;
   @Valid
   @NotEmpty(message = "attributes" + MessageConstants.INVALID_MISSING)
    private List<ProductAttributeDTO> productAttributes = new ArrayList<>();
   @Valid
   @NotEmpty(message = "images " + MessageConstants.INVALID_MISSING)
    private List<ProductImageDTO> productImages = new ArrayList<>();
}

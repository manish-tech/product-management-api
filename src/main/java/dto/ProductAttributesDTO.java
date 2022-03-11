package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductAttributesDTO {
    private String name;
    private String value;
}

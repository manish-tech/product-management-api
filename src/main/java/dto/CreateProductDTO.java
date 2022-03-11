package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateProductDTO {
    private String id;
    private String name;
    private String description;
    private String brand;
    private Float length;
    private Float width;
    private Float height;
    private List<ProductAttributesDTO> productAttributes = new ArrayList<>();
    private List<ProductImagesDTO> productImages = new ArrayList<>();
}

package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductImagesDTO {
    private String imageType;
    private String imageUrl;
}

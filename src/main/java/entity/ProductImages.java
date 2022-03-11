package entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ProductImages implements Serializable {
    private String imageType;
    private String imageUrl;
}
package entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Product {
    @Id
    private String productId;
    private String name;
    private String description;
    private String brand;
    private Float length;
    private Float width;
    private Float height;
    private List<ProductAttributes> productAttributes = new ArrayList<>();
    private List<ProductImages> productImages = new ArrayList<>();
}

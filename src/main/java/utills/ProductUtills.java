package utills;

import dto.CreateProductDTO;
import entity.Product;
import entity.ProductAttributes;
import entity.ProductImages;

import java.util.stream.Collectors;

public class ProductUtills {

    public static Product dtoToEntity(CreateProductDTO createProductDTO){
        Product product = Product.builder()
                .productId(createProductDTO.getId())
                .name(createProductDTO.getName())
                .description(createProductDTO.getDescription())
                .brand(createProductDTO.getBrand())
                .length(createProductDTO.getLength())
                .width(createProductDTO.getWidth())
                .height(createProductDTO.getHeight())
                .productAttributes(createProductDTO.getProductAttributes()
                        .stream()
                        .map((attribute)->{
                            return ProductAttributes.builder()
                                    .name(attribute.getName())
                                    .value(attribute.getValue())
                                    .build();
                        }).collect(Collectors.toList())
                )
                .productImages(createProductDTO.getProductImages()
                        .stream()
                        .map((image)->{
                            return ProductImages.builder()
                                    .imageType(image.getImageType())
                                    .imageUrl(image.getImageUrl())
                                    .build();

                        }).collect(Collectors.toList())
                )
                .build();
        return product;
    }
}

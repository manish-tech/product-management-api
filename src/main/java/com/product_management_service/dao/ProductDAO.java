package com.product_management_service.dao;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.product_management_service.constants.Names;
import com.product_management_service.constants.Operators;
import com.product_management_service.dto.UpdateProductDTO;
import com.product_management_service.dto.UpdateResponseDTO;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.entity.ProductImage;
import com.product_management_service.exceptions.ErrorCode;
import com.product_management_service.exceptions.SystemException;
import com.product_management_service.utills.AttributeUtills;
import com.product_management_service.utills.ProductImageUtills;
import com.product_management_service.utills.ProductUtills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.product_management_service.repositories.ProductRepository;

import java.util.List;

@Component
public class ProductDAO {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Product> findProductById(String productId){
        return productRepository.findById(productId);
    }
    public Mono<Product> saveProduct(Product product){
        return productRepository.save(product);
    }

    public  Mono<UpdateResponseDTO> editProduct(UpdateProductDTO updateProductDTO){
        Mono<Product> productMono = findProductById(updateProductDTO.getProductId());
        return productMono
                .hasElement()
                .flatMap((exist)->{
                    if (Boolean.FALSE.equals(exist)){
                        return Mono.error(
                                SystemException.builder()
                                        .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                        .message("product doesn't exists")
                                        .build()
                        );
                    }

                   return productMono.flatMap((product)->{
                       if(AttributeUtills.checkDuplicateAttributeName(product.getProductAttributes(),updateProductDTO.getProductAttributes())){
                           return Mono.error(SystemException.builder()
                                   .errorCode(ErrorCode.ATTRIBUTE_NAME_ALREADY_EXISTS)
                                   .message("duplicate attribute name found")
                                   .build()
                           );
                       }
                       else if(ProductImageUtills.checkDuplicateImage(product.getProductImages(),updateProductDTO.getProductImages())){
                           return Mono.error(SystemException.builder()
                                   .errorCode(ErrorCode.IMAGE_CONFLICT)
                                   .message("duplicate image found")
                                   .build()
                           );
                       }
                       else {
                           product = ProductUtills.dtoToEntity(updateProductDTO);
                           return productRepository.save(product);
                       }
                   });
                }).map((product -> {
                    return UpdateResponseDTO.builder()
                            .data(product)
                            .status(Names.SUCCESS.getValue())
                            .modifiedCount(1L)
                            .build();
                }));
    }

    public Mono<UpdateResponseDTO> addAttribute(ProductAttribute productAttribute,String productId){
        Mono<Product> productMono = productRepository.findById(productId);
        return productMono.hasElement()
                .flatMap((exists)->{
                    if (Boolean.FALSE.equals(exists)){
                        return Mono.error(SystemException.builder()
                                .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                .message("product doesn't exists")
                                .build());
                    }

                    return productMono.flatMap((product -> {
                        Boolean isAttributeNameExists = product.getProductAttributes()
                                .stream()
                                .parallel()
                                .anyMatch((attribute)->{
                                    if(attribute.getName().equals(productAttribute.getName())){
                                        return Boolean.TRUE;
                                    }
                                    return Boolean.FALSE;
                                });
                        if(Boolean.TRUE.equals(isAttributeNameExists)){
                            return Mono.error(SystemException.builder()
                                    .errorCode(ErrorCode.ATTRIBUTE_NAME_ALREADY_EXISTS)
                                    .message("this attribute name already exists")
                                    .build()) ;
                        }
                        Query query = new Query(Criteria.where("_id").is(productId));
                        Update update = new Update();
                        update.addToSet(Names.PRODUCT_ATTRIBUTES.getValue(),productAttribute);
                        Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                        return updateResult.flatMap((r)->{
                            if(r.getModifiedCount() == 0L){
                                return Mono.error(SystemException.builder()
                                        .errorCode(ErrorCode.ATTRIBUTE_CONFLICT)
                                        .message("attribute already exists")
                                        .build());
                            }
                            return Mono.just(UpdateResponseDTO.builder()
                                    .data(productAttribute)
                                    .modifiedCount(r.getModifiedCount())
                                    .status(Names.SUCCESS.getValue())
                                    .build());
                        });
                    }));

                });
    }
    public Mono<UpdateResponseDTO> addImageUrl(ProductImage productImage, String productId){
         return productRepository.existsById(productId)
                .flatMap((exists)->{
                    if (Boolean.FALSE.equals(exists)){
                        return Mono.error(
                                SystemException.builder()
                                .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                .message("product doesn't exists")
                                .build()
                        );
                    }
                    Query query = new Query(Criteria.where("_id").is(productId));
                    Update update = new Update();
                    update.addToSet(Names.PRODUCT_IMAGES.getValue(),productImage);
                    Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                    return updateResult.flatMap((r)->{
                        if(r.getModifiedCount() == 0L){
                            return Mono.error(SystemException.builder()
                                    .errorCode(ErrorCode.IMAGE_CONFLICT)
                                    .message("Image already exists")
                                    .build());
                        }

                        return Mono.just(UpdateResponseDTO.builder()
                                .data(productImage)
                                .status(Names.SUCCESS.getValue())
                                .modifiedCount(r.getModifiedCount())
                                .build());
                    });
                });
    }

    public Mono<UpdateResponseDTO> updateAttribute(String currentName,String productId,ProductAttribute productAttribute){
        Mono<Product> productMono = productRepository.findById(productId);
        return productMono.hasElement()
            .flatMap((exists)-> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(SystemException.builder()
                            .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                            .message("product doesn't exists")
                            .build()
                    );
                }
                return productMono.flatMap((product -> {
                    Boolean isAttributeNameExists = product.getProductAttributes()
                            .stream()
                            .parallel()
                            .anyMatch((attribute)->{
                                if(attribute.getName().equals(currentName)){
                                    attribute.setName(productAttribute.getName());
                                    return Boolean.TRUE;
                                }
                                return Boolean.FALSE;
                            });
                    Boolean isDuplicateAttributeNamePossible = AttributeUtills.checkDuplicateAttributeName(product.getProductAttributes(),productAttribute);
                    Query query = new Query(Criteria.where("_id").is(productId))
                            .addCriteria(Criteria.where(Names.PRODUCT_ATTRIBUTES.getValue()+".name").is(currentName));
                    Update update = new Update();
                    if(isDuplicateAttributeNamePossible){
                        return Mono.error(SystemException.builder()
                                                .errorCode(ErrorCode.ATTRIBUTE_NAME_ALREADY_EXISTS)
                                                .message("attribute name already exists")
                                                .build()
                        );
                    }
                    else if(Boolean.TRUE.equals(isAttributeNameExists) ){
                        update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.NAME.getValue(),productAttribute.getName());
                        update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.VALUE.getValue(),productAttribute.getValue());
                        Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                        return updateResult.flatMap((r)->{
                            if(r.getModifiedCount() == 0L){
                                    return Mono.error(SystemException.builder()
                                            .errorCode(ErrorCode.ATTRIBUTE_UPDATE_CONFLICT)
                                            .message("updated attribute by same values")
                                            .build());
                            }
                            return Mono.just(UpdateResponseDTO.builder()
                                    .data(productAttribute)
                                    .modifiedCount(r.getModifiedCount())
                                    .status(Names.SUCCESS.getValue())
                                    .build());
                        });
                    }
                    else{
                        return Mono.error(SystemException.builder()
                                .errorCode(ErrorCode.ATTRIBUTE_NOT_FOUND)
                                .message("attribute name doesn't exists")
                                .build());
                    }

                }));
            });
    }


    public Mono<UpdateResponseDTO> deleteAttribute(String productId,String attributeName){
        return productRepository.existsById(productId)
                .flatMap((exists)->{
                    if(Boolean.FALSE.equals(exists)){
                        return Mono.error(SystemException.builder()
                                .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                .message("product doesn't exists")
                                .build()
                        );

                    }
                    Query query = new Query(Criteria.where("_id").is(productId));
                    Update update = new Update();
                    update.pull(Names.PRODUCT_ATTRIBUTES.getValue(),new BasicDBObject(Names.NAME.getValue(),attributeName));
                    Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                    return updateResult.flatMap((r)->{
                        if(r.getModifiedCount() == 0L){
                            return Mono.error(SystemException.builder()
                                    .errorCode(ErrorCode.ATTRIBUTE_NOT_FOUND)
                                    .message("attribute doesn't exists")
                                    .build());
                        }
                        return Mono.just(UpdateResponseDTO.builder()
                                .modifiedCount(r.getModifiedCount())
                                .status(Names.SUCCESS.getValue())
                                .build());
                    });
                });
    }

    public Mono<UpdateResponseDTO> removeProduct(String productId){
        return findProductById(productId)
                .hasElement()
                .flatMap((exists)->{
                    if(Boolean.FALSE.equals(exists)){
                        return Mono.error(SystemException.builder()
                                .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                .message("product doesn't exists")
                                .build()
                        );
                    }
                    return productRepository.deleteById(productId)
                            .then(Mono.just(UpdateResponseDTO.builder()
                                    .modifiedCount(1L)
                                    .status(Names.SUCCESS.getValue())
                                    .build())
                            )
                            .map((updateResponseDTO)->{
                                return updateResponseDTO;
                            });
                });
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll()
                .switchIfEmpty(Flux.error(
                        SystemException.builder()
                                .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                .message("no products found")
                                .build()
                ));

    }
}

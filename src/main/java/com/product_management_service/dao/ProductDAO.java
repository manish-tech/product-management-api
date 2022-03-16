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
        return findProductById(updateProductDTO.getProductId())
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
                    Product product = ProductUtills.dtoToEntity(updateProductDTO);
                    return productRepository.save(product);
                }).map((product -> {
                    return UpdateResponseDTO.builder()
                            .data(product)
                            .status(Names.SUCCESS.getValue())
                            .modifiedCount(1L)
                            .build();
                }));
    }

    public Mono<UpdateResponseDTO> addAttribute(ProductAttribute productAttribute,String productId){
        return productRepository.existsById(productId)
                .flatMap((exists)->{
                    if (Boolean.FALSE.equals(exists)){
                        return Mono.error(SystemException.builder()
                                .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                                .message("product doesn't exists")
                                .build());
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

//    public  Update setIndividualAttributesOfArrayOfObjects(String classFullName,T entityObject,String arrayName){
//        List<String> fieldNames = ProductUtills.getFeildNamesOfAClass(classFullName);
//        Update update = new Update();
//
//
//            fieldNames.forEach((fieldName)->{
//                update.set(arrayName+"."+ Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+fieldName,entityObject.getName());
//            });
//
//
//
//
//
//        update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.VALUE.getValue(),productAttribute.getValue());
//        return update;
//    }

    public Mono<UpdateResponseDTO> updateAttribute(String name,String productId,ProductAttribute productAttribute){
        return productRepository.existsById(productId)
            .flatMap((exists)-> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(SystemException.builder()
                            .errorCode(ErrorCode.PRODUCT_NOT_FOUND)
                            .message("product doesn't exists")
                            .build()
                    );
                }
                Query query = new Query(Criteria.where("_id").is(productId))
                        .addCriteria(Criteria.where(Names.PRODUCT_ATTRIBUTES.getValue()+".name").is(name));
                Update update = new Update();
                update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.NAME.getValue(),productAttribute.getName());
                update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.VALUE.getValue(),productAttribute.getValue());
                Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class).doOnError((t)->System.out.println(t.getMessage()));
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
                            .map((Void v)->{
                                return UpdateResponseDTO.
                                        builder()
                                        .modifiedCount(1L)
                                        .status(Names.SUCCESS.getValue())
                                        .build();
                            }
                    );
                });
    }
    public Flux<Product> getAllProducts(){
        return  productRepository.findAll();
    }




}

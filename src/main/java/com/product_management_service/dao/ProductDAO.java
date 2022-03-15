package com.product_management_service.dao;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import com.product_management_service.constants.Names;
import com.product_management_service.constants.Operators;
import com.product_management_service.dto.ProductImageDTO;
import com.product_management_service.dto.UpdateProductDTO;
import com.product_management_service.entity.Product;
import com.product_management_service.entity.ProductAttribute;
import com.product_management_service.entity.ProductImage;
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

    public  Mono<Product> editProduct(UpdateProductDTO updateProductDTO){
        return findProductById(updateProductDTO.getProductId())
                .hasElement()
                .flatMap((exist)->{
                    if (Boolean.FALSE.equals(exist)){
                        return Mono.error(new RuntimeException("product does not exists"));
                    }
                    Product product = ProductUtills.dtoToEntity(updateProductDTO);
                    return productRepository.save(product);
                });
    }

    public Mono<Long> addAttribute(ProductAttribute productAttribute,String productId){
        return productRepository.existsById(productId)
                .flatMap((exists)->{
                    if (Boolean.FALSE.equals(exists)){
                        return Mono.error(new RuntimeException("product doesn't exist"));
                    }
                    Query query = new Query(Criteria.where("_id").is(productId));
                    Update update = new Update();
                    update.addToSet(Names.PRODUCT_ATTRIBUTES.getValue(),productAttribute);
                    Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                    return updateResult.map((r)->{
                        return r.getMatchedCount();
                    });
                });
    }
    public Mono<Long> addImageUrl(ProductImage productImage, String productId){
        return productRepository.existsById(productId)
                .flatMap((exists)->{
                    if (Boolean.FALSE.equals(exists)){
                        return Mono.error(new RuntimeException("product doesn't exist"));
                    }
                    Query query = new Query(Criteria.where("_id").is(productId));
                    Update update = new Update();
                    update.addToSet(Names.PRODUCT_IMAGES.getValue(),productImage);
                    Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                    return updateResult.map((r)->{
                        return r.getMatchedCount();
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

    public Mono<Long> updateAttribute(String name,String productId,ProductAttribute productAttribute){
        return productRepository.existsById(productId)
            .flatMap((exists)-> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new RuntimeException("product doesn't exist"));
                }
                Query query = new Query(Criteria.where("_id").is(productId))
                        .addCriteria(Criteria.where(Names.PRODUCT_ATTRIBUTES.getValue()+".name").is(name));
                Update update = new Update();
                update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.NAME.getValue(),productAttribute.getName());
                update.set(Names.PRODUCT_ATTRIBUTES.getValue()+"."+Operators.POSITIONAL_UPDATE_OPERATOR.getValue()+"."+Names.VALUE.getValue(),productAttribute.getValue());
                Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class).doOnError((t)->System.out.println(t.getMessage()));
                return updateResult.map((r)->r.getModifiedCount());
            });
    }


    public Mono<Long> deleteAttribute(String productId,String attributeName){
        return productRepository.existsById(productId)
                .flatMap((exists)->{
                    if(Boolean.FALSE.equals(exists)){
                        return Mono.error(new RuntimeException("product doesn't exists"));
                    }
                    Query query = new Query(Criteria.where("_id").is(productId));
                    Update update = new Update();
                    update.pull(Names.PRODUCT_ATTRIBUTES.getValue(),new BasicDBObject(Names.NAME.getValue(),attributeName));
                    Mono<UpdateResult> updateResult = reactiveMongoTemplate.updateFirst(query,update,Product.class);
                    return updateResult.map((result)->result.getModifiedCount());
                });
    }

    public Mono<Void> removeProduct(String productId){
        return findProductById(productId)
                .hasElement()
                .flatMap((exists)->{
                    if(Boolean.FALSE.equals(exists)){
                        return Mono.error(new RuntimeException("does not exist"));
                    }
                    return productRepository.deleteById(productId);
                });
    }
    public Flux<Product> getAllProducts(){
        return  productRepository.findAll();
    }




}

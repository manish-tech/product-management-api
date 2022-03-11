package service;

import dto.CreateProductDTO;
import entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.money.MonetaryAmountFormatter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import repositories.ProductRepository;
import utills.ProductUtills;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public Mono<Product> createProduct(CreateProductDTO productDTO){
        return productRepository.save(ProductUtills.dtoToEntity(productDTO)).doOnError((throwable)-> {

        });
    }

}

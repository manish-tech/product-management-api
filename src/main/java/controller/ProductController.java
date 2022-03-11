package controller;

import dto.CreateProductDTO;
import entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import service.ProductService;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Mono<Product> createProduct(@RequestBody CreateProductDTO createProductDTO){
        return productService.createProduct(createProductDTO);
    }
}

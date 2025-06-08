package com.ecommers.product.Repository;

import com.ecommers.product.Entity.Product;
import com.ecommers.product.integrationTestClasses.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByName(String name);

//    List<Product> findAllOrderByPriceDesc();
    List<Product>  findAllByPriceIsNotNullOrderByPriceDesc();;
}

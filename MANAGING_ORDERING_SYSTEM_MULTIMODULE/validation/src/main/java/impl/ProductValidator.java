package impl;


import exceptions.MyException;
import generic.Validator;
import model.Product;
import repositories.ProductRepository;

import static exceptions.ExceptionCode.PRODUCTS;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {
        ProductRepository productRepository = new ProductRepositoryImpl();

        if (productRepository.findAll()
                .stream()
                .anyMatch(x -> x.getName().equals(product.getName())
                        && x.getCategory().getName().equals(product.getCategory().getName())
                &&x.getProducer().getName().equals(product.getProducer().getName())
                &&x.getProducer().getCountry().getName().equals(product.getProducer().getCountry().getName()))) {

            throw new MyException(PRODUCTS, "ADD PRODUCT EXCEPTION - PRODUCT IS ALLREADY IN TABLE");
        }
    }
}

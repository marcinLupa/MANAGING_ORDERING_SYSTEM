package repositories;

import generic.GenericRepository;
import model.Product;

import java.util.Optional;

public interface ProductRepository extends GenericRepository<Product> {
    Optional<Product> findByName(String productName);

}

package repositories;

import generic.GenericRepository;
import model.Shop;

import java.util.Optional;

public interface ShopRepository extends GenericRepository<Shop> {
    Optional<Shop> findByName(String productName);

}

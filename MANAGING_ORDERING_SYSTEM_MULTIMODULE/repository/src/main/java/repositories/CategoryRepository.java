package repositories;

import generic.GenericRepository;
import model.Category;

import java.util.Optional;

public interface CategoryRepository extends GenericRepository<Category> {
    Optional<Category> findByName(String categoryName);
}

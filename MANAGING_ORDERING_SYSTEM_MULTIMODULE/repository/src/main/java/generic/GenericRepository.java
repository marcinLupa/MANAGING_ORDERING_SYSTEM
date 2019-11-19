package generic;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {
    Optional<T> addOrUpdate(T t);
    Optional<T> delete(Long id);
    Optional<T> findById(Long id);
    List<T> findAll();
   void deleteAll();
   List<String> allEntitiesName();
    Optional<T> findLast();




}

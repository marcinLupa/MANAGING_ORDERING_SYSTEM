package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.Category;
import repositories.CategoryRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl extends AbstractGenericRepository<Category> implements CategoryRepository {
    @Override
    public Optional<Category> findByName(String categoryName) {

        Optional<Category> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            List<Category> categories = entityManager
                    .createQuery("select c from Category c where c.name=:name", Category.class)
                    .setParameter("name", categoryName)
                    .getResultList();
            if (!categories.isEmpty()) {
                op = Optional.of(categories.get(0));
            }

            entityTransaction.commit();
            return op;
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "FIND BY NAME EXCEPTION");
        }


    }

    public void deleteAll() {

            EntityTransaction entityTransaction = entityManager.getTransaction();
             try {
            entityTransaction.begin();
            entityManager.createQuery("UPDATE Product p SET p.category=null").executeUpdate();
            entityManager.createQuery("DELETE Category c").executeUpdate();
            entityTransaction.commit();

            } catch (Exception e) {
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION " );
            }
    }
}


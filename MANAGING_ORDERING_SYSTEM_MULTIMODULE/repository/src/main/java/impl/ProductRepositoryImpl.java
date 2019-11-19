package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;

import model.Product;
import repositories.ProductRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl extends AbstractGenericRepository<Product> implements ProductRepository {


    @Override
    public Optional<Product> findByName(String product) {
            Optional<Product> op = Optional.empty();
            EntityTransaction entityTransaction = entityManager.getTransaction();

          try {
                entityTransaction.begin();
                List<Product> products = entityManager
                        .createQuery("select p from Product p where p.name=:name", Product.class)
                        .setParameter("name", product)
                        .getResultList();
                if (!products.isEmpty()) {
                    op = Optional.of(products.get(0));
                }

                entityTransaction.commit();
            return op;

        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
        }

    }

    @Override
    public void deleteAll() {
            List<Product> products = findAll();

            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.createQuery("UPDATE CustomerOrder co SET co.product=null").executeUpdate();
                entityManager.createQuery("UPDATE Stock s SET s.product=null").executeUpdate();
                products.forEach(entityManager::remove);
                entityTransaction.commit();

            } catch (Exception e) {
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
            }

    }
}

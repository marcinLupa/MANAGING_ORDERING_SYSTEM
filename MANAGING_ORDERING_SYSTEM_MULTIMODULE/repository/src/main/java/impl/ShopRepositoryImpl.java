package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.Shop;
import repositories.ShopRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ShopRepositoryImpl extends AbstractGenericRepository<Shop> implements ShopRepository {
    @Override
    public Optional<Shop> findByName(String shopName) {

        Optional<Shop> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            List<Shop> shops = entityManager
                    .createQuery("select s from Shop s where s.name=:name", Shop.class)
                    .setParameter("name", shopName)
                    .getResultList();
            if (!shops.isEmpty()) {
                op = Optional.of(shops.get(0));
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

            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.createQuery("UPDATE Stock s SET s.shop=null").executeUpdate();
                entityManager.createQuery("DELETE Shop s").executeUpdate();
                entityTransaction.commit();

            } catch (Exception e) {
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
            }

    }
}

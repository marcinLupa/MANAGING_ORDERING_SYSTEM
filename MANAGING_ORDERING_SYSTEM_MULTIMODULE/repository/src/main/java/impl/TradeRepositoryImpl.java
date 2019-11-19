package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;

import model.Trade;
import repositories.TradeRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class TradeRepositoryImpl extends AbstractGenericRepository<Trade> implements TradeRepository {
    @Override
    public Optional<Trade> findByName(String tradeName) {
            Optional<Trade> op = Optional.empty();
            EntityTransaction entityTransaction = entityManager.getTransaction();

       try {
                entityTransaction.begin();
                List<Trade> trades = entityManager
                        .createQuery("select t from Trade t where t.name=:name", Trade.class)
                        .setParameter("name", tradeName)
                        .getResultList();
                if (!trades.isEmpty()) {
                    op = Optional.of(trades.get(0));
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

    @Override
    public void deleteAll() {

            EntityTransaction entityTransaction = entityManager.getTransaction();
            try {
                entityTransaction.begin();
                entityManager.createQuery("UPDATE Producer p SET p.trade=null").executeUpdate();
                entityManager.createQuery("DELETE Trade t").executeUpdate();
                entityTransaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
            }
    }
}


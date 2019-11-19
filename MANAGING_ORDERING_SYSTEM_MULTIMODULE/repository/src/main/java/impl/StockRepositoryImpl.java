package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.Stock;
import repositories.StockRepository;

import javax.persistence.EntityTransaction;

public class StockRepositoryImpl extends AbstractGenericRepository<Stock> implements StockRepository {
    @Override
    public void deleteAll() {

            EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
                entityTransaction.begin();
                entityManager.createQuery("DELETE Stock s").executeUpdate();
                entityTransaction.commit();

            } catch (Exception e) {
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
            }

    }

}

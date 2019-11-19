package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.CustomerOrder;
import repositories.CustomerOrderRepository;

import javax.persistence.EntityTransaction;

public class CustomerOrderRepositoryImpl extends AbstractGenericRepository<CustomerOrder>
        implements CustomerOrderRepository {
    @Override
    public void deleteAll() {


        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();

            entityManager.createQuery("DELETE CustomerOrder co").executeUpdate();
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

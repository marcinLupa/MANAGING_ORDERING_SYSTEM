package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.Customer;
import repositories.CustomerRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl extends AbstractGenericRepository<Customer> implements CustomerRepository {
    @Override
    public Optional<Customer> findByName(String customerName) throws MyException {
        Optional<Customer> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            List<Customer> payments = entityManager
                    .createQuery("select c from Customer c where c.name=:name", Customer.class)
                    .setParameter("name", customerName)
                    .getResultList();
            if (!payments.isEmpty()) {
                op = Optional.of(payments.get(0));
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
            entityManager.createQuery("UPDATE CustomerOrder o SET o.customer=null").executeUpdate();
            entityManager.createQuery("DELETE Customer c").executeUpdate();
            entityTransaction.commit();

            } catch (Exception e) {
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
            }

    }
}

package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.Payment;
import repositories.PaymentRepository;
import utils.EPayment;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class PaymentRepositoryImpl extends AbstractGenericRepository<Payment> implements PaymentRepository {
    @Override
    public Optional<Payment> findByName(EPayment ePayment) {
        Optional<Payment> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            List<Payment> payments = entityManager
                    .createQuery("select p from Payment p where p.payment=:name", Payment.class)
                    .setParameter("name", ePayment)
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
            entityManager.createQuery("UPDATE CustomerOrder o SET o.payment=null").executeUpdate();
            entityManager.createQuery("DELETE Payment p").executeUpdate();
            entityTransaction.commit();

        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
        }

    }
}

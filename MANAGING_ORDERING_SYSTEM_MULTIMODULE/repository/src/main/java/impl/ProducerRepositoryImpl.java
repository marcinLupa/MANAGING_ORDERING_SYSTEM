package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;
import model.Producer;
import repositories.ProducerRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ProducerRepositoryImpl extends AbstractGenericRepository<Producer> implements ProducerRepository {
    @Override
    public Optional<Producer> findByName(String producerName) {
        Optional<Producer> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            List<Producer> producers = entityManager
                    .createQuery("select p from Producer p where p.name=:name", Producer.class)
                    .setParameter("name", producerName)
                    .getResultList();
            if (!producers.isEmpty()) {
                op = Optional.of(producers.get(0));
            }

            entityTransaction.commit();
//            } catch (Exception e) {
//                if (entityTransaction != null) entityTransaction.rollback();
//                throw new MyException(ExceptionCode.REPOSITORY, "FIND BY NAME EXCEPTION");
//            } finally {
//                entityManager.close();
//            }
            return op;
        } catch (Exception e) {
            throw new MyException(ExceptionCode.REPOSITORY, "FIND BY NAME EXCEPTION");
        }
    }

    @Override
    public Optional<Producer> findByCountry(String countryName) {
        Optional<Producer> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            List<Producer> producers = entityManager
                    .createQuery("select p from Producer p where p.country.name=:name", Producer.class)
                    .setParameter("name", countryName)
                    .getResultList();
            if (!producers.isEmpty()) {
                op = Optional.of(producers.get(0));
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
            entityManager.createQuery("UPDATE Product p SET p.producer=null").executeUpdate();
            entityManager.createQuery("DELETE Producer p").executeUpdate();
            entityTransaction.commit();

            } catch (Exception e) {
                if (entityTransaction != null) {
                    entityTransaction.rollback();
                }
                throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
            }

    }
}

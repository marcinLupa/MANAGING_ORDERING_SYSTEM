package impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import generic.AbstractGenericRepository;

import model.Country;
import repositories.CountryRepository;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class CountryRepositoryImpl extends AbstractGenericRepository<Country> implements CountryRepository {
    @Override
    public Optional<Country> findByName(String countryName) {

        Optional<Country> op = Optional.empty();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {

            entityTransaction.begin();
            List<Country> countries = entityManager
                    .createQuery("select c from Country c where c.name=:name", Country.class)
                    .setParameter("name", countryName)
                    .getResultList();
            if (!countries.isEmpty()) {
                op = Optional.of(countries.get(0));
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
            entityManager.createQuery("UPDATE Customer c SET c.country=null").executeUpdate();
            entityManager.createQuery("UPDATE Producer p SET p.country=null").executeUpdate();
            entityManager.createQuery("UPDATE Shop s SET s.country=null").executeUpdate();
            entityManager.createQuery("DELETE Country c").executeUpdate();
            entityTransaction.commit();

        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL EXCEPTION ");
        }
    }
}


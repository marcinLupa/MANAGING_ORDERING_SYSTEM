package generic;

import connections.DbConnection;
import exceptions.ExceptionCode;
import exceptions.MyException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractGenericRepository<T> implements GenericRepository<T> {

    protected final EntityManager entityManager = DbConnection.getInstance().getEntityManager();

    private final Class<T> type = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public Optional<T> addOrUpdate(T t) {
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            Optional<T> op = Optional.empty();
            entityTransaction.begin();
            op = Optional.of((T) entityManager.merge(t));
            entityTransaction.commit();

            return op;
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "ADD OR UPDATE EXCEPTION");
        }
    }

    @Override
    public Optional<T> delete(Long id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Optional<T> op = Optional.empty();
        try {
            entityTransaction.begin();
            op = Optional.of((T) entityManager.find(type, id));
            entityManager.remove(op.orElseThrow(() -> new MyException(ExceptionCode.REPOSITORY, "DELETE EXCEPTION")));
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "DELETE EXCEPTION");
        }
        return op;
    }

    @Override
    public Optional<T> findById(Long id) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            Optional<T> op = Optional.empty();
            entityTransaction.begin();
            op = Optional.of((T) entityManager.find(type, id));
            entityTransaction.commit();
            return op;
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "FIND BY ID EXCEPTION");
        }
    }

    @Override
    public List<T> findAll() {

        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            List<T> elements = new ArrayList<>();
            entityTransaction.begin();
            elements = entityManager
                    .createQuery("select t from " + type.getCanonicalName() + " t", type)
                    .getResultList();
            entityTransaction.commit();
            return elements;

        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "FIND ALL EXCEPTION");

        }
    }

    @Override
    public void deleteAll() {

        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            Query query = entityManager.createQuery("DELETE FROM " + type.getCanonicalName());
            query.executeUpdate();
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "DELETE ALL NAME EXCEPTION");

        }
    }

    @SuppressWarnings("JoinDeclarationAndAssignmentJava")
    @Override
    public List<String> allEntitiesName() {
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            List<String> tablesName;
            entityTransaction.begin();

            tablesName = entityManager.createNativeQuery("select table_name from information_schema.tables WHERE table_schema = 'per01';").getResultList();

            entityTransaction.commit();
            return tablesName;
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "ALL ENTITIES NAME EXCEPTION");
        }

    }

    @Override
    public Optional<T> findLast() {

        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            Optional<T> op = Optional.empty();
            entityTransaction.begin();
            op = Optional.of((T) entityManager.createQuery("select t from " + type.getCanonicalName() + " t" + " order by  t.id ", type)
                    .setMaxResults(1)
                    .getResultList()
                    .get(0));
            entityTransaction.commit();
            return op;

        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            throw new MyException(ExceptionCode.REPOSITORY, "ALL ENTITIES NAME EXCEPTION");
        }


    }
}

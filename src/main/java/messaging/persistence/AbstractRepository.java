package messaging.persistence;

import messaging.persistence.entities.Identification;
import org.hibernate.*;

import javax.persistence.NoResultException;

abstract class AbstractRepository<T extends Identification> {
    final Class<T> entityClass;
    final String entityName;
    private final SessionFactory sessionFactory;



    protected AbstractRepository(Class<T> entity) {
        sessionFactory = HibernateFactory.getHibernateFactory().getSessionFactory();
        this.entityClass = entity;
        entityName = entity.getSimpleName();
    }


    String saveEntity(T entity) {
        Session session = startSession();
        Transaction transaction = null;
        String id = entity.getId();
        try {
            transaction = session.beginTransaction();
            if (id == null) {
                id = (String) session.save(entity);
            } else {
                session.saveOrUpdate(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    boolean deleteEntity(T entity) {
        Session session = startSession();
        Transaction transaction = null;
        boolean result = false;
        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
            result = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    Session startSession() {
        Session session = sessionFactory.openSession();
        session.setHibernateFlushMode(FlushMode.COMMIT);
        return session;
    }

    T getEntityById(String id){
        Session session = startSession();
        Transaction transaction = null;
        T result = null;
        try {
            transaction = session.beginTransaction();
            T t = session.get(entityClass, id);
            transaction.commit();
        } catch (NoResultException nre) {
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
}

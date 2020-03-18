package messaging.persistence;

import messaging.persistence.entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;

class UserRepository extends AbstractRepository<UserEntity> {
    private static UserRepository userRepository;
    private static volatile boolean alive;
    private static final String FIND_BY_USERNAME = "from %s as e where e.username =:username";
    private static final String USERNAME = "username";

    private UserRepository() {
        super(UserEntity.class);
    }

    static UserRepository getUserRepository() {
        UserRepository repository;
        if (!alive){
            createRepository();
        }
        repository = userRepository;
        return repository;
    }

    private static void createRepository() {
        userRepository = new UserRepository();
    }

    UserEntity findUserByUsername(String username){
        UserEntity entity = null;
        Session session = startSession();
        Transaction transaction = null;
        String query = String.format(FIND_BY_USERNAME,entityName);
        try {
            transaction = session.beginTransaction();
            entity = (UserEntity) session.createQuery(query).setParameter(USERNAME,username).getSingleResult();
            transaction.commit();
        }catch (NoResultException nre){
            if (transaction != null){
                transaction.rollback();
            }
        }catch (Exception e)
        {
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();

        }finally {
            session.close();
        }
        return entity;
    }

}

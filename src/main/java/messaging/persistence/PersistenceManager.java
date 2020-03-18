package messaging.persistence;

import messaging.authentication.User;
import messaging.messages.AbstractMessage;
import messaging.persistence.entities.MessageEntity;
import messaging.persistence.entities.UserEntity;

public class PersistenceManager {
    private static PersistenceManager manager;
    private static volatile boolean alive;
    private static volatile boolean initialized;

    private PersistenceManager() {
        alive = true;
    }

    public static PersistenceManager getManager() {
        PersistenceManager persistenceManager;
        if (!alive || !initialized) {
            createManager();
            HibernateFactory.getHibernateFactory();
            initialized = true;
        }
        persistenceManager = manager;
        return persistenceManager;
    }

    private static void createManager() {
        manager = new PersistenceManager();
    }

    public User saveUser(User user) {
        UserEntity entity = Mapper.getMapper().map(user, UserEntity.class);
        String id = UserRepository.getUserRepository().saveEntity(entity);
        user.setId(id);
        return user;
    }

    public User getUser(String id) {
        if (id == null){
            return null;
        }
        UserEntity entity = UserRepository.getUserRepository().getEntityById(id);
        if (entity == null){
            return null;
        }
        return Mapper.getMapper().map(entity,User.class);
    }
    public User getUserByUsername(String username) {
        if (username == null){
            return null;
        }
        UserEntity entity = UserRepository.getUserRepository().findUserByUsername(username);
        if (entity == null){
            return null;
        }
        return Mapper.getMapper().map(entity,User.class);
    }

    public boolean deleteUser(User user) {
        UserEntity entity = Mapper.getMapper().map(user, UserEntity.class);
        return UserRepository.getUserRepository().deleteEntity(entity);
    }

    public User updateUser(User user) {
        return saveUser(user);
    }

    public AbstractMessage persistMessage(AbstractMessage message) {
        MessageEntity entity = Mapper.getMapper().map(message, MessageEntity.class);
        UserEntity receiver = UserRepository.getUserRepository().findUserByUsername((entity.getReceiver().getUsername()));
        if (receiver == null) {
            return message;
        }

        UserEntity sender =UserRepository.getUserRepository().findUserByUsername(entity.getSender().getUsername());
        entity.setSender(sender);
        entity.setReceiver(receiver);
        String id = MessageRepository.getMessageRepository().saveEntity(entity);
        message.setId(id);
        return message;
    }
}

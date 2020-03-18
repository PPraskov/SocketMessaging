package messaging.persistence;

import messaging.persistence.entities.MessageEntity;
import messaging.persistence.entities.UserEntity;

class MessageRepository extends AbstractRepository<MessageEntity> {
    private static MessageRepository messageRepository;
    private static volatile boolean alive;

    private MessageRepository() {
        super(MessageEntity.class);
    }

    static MessageRepository getMessageRepository() {
        MessageRepository repository;
        if (!alive){
            createRepository();
        }
        repository = messageRepository;
        return repository;
    }

    private static void createRepository() {
        messageRepository = new MessageRepository();
    }
}

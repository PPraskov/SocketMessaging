import client.messages.InputMessage;
import client.messages.MessageReceiving;
import client.messages.MessageSending;
import client.User;
import client.messages.OutputMessage;
import messaging.MessagingManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SocketTesting {

    private static MessagingManager manager;

    public SocketTesting() {
    }

    @BeforeClass
    public static void initialize() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> aClass = MessagingManager.class;
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        Constructor c = declaredConstructors[0];
        Object o = c.newInstance();
        manager = (MessagingManager) o;
        Runner runner = new Runner(o);
        new Thread(runner).start();
    }

    @AfterClass
    public static void stopApp() {
        manager.stopApplication();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Server socket is stopped!");
    }

    @Test
    public void testTwoGuysChatting() {
        boolean passed = false;
        try {
            User user1 = new User("Papi","passPapi");
            User user2 = new User("Nick","passNick");
            String messageToFirst = "testTo" + user1.getName();
            String messageToSecond = "TestTo" + user2.getName();
            user1.sendMessage(user2.getName(), messageToSecond);
            user2.sendMessage(user1.getName(), messageToFirst);
            InputMessage user2Inbox = user2.getInbox().getMessage();
            InputMessage user1Inbox = user1.getInbox().getMessage();
            MessageReceiving testFromFirst = new MessageReceiving(user1.getName(), messageToSecond);
            MessageReceiving testFromSecond = new MessageReceiving(user2.getName(), messageToFirst);
            Thread.sleep(1000);
            user1.stopListening();
            user2.stopListening();

            if (user2Inbox.equals(testFromFirst) && user1Inbox.equals(testFromSecond)) {
                passed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue("Fucked up", passed);
    }

    @Test
    public void otherGuyNotActive() {
        try {
            User user1 = new User("Bumba","bumba");
            user1.sendMessage("nonExistent", "do you even exist?");
            List<InputMessage> user1Inbox = user1.getInbox().getAllMessages();
            MessageReceiving messageReceivingTest = new MessageReceiving("Messaging Server", "User not found!");
            user1.stopListening();
            boolean passed = false;
            if (user1Inbox.get(0).equals(messageReceivingTest)) {
                passed = true;
            }
            Assert.assertTrue("Fucked up!", passed);
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tenGuysChatting() throws InterruptedException {
        final int usersCount = 10;
        UsernameGenerator generator = new UsernameGenerator();
        generator.setMax(usersCount);
        List<UserThreadTest> userThreadTests = new ArrayList<>();
        for (int i = 0; i < usersCount; i++) {
            UserThreadTest test = new UserThreadTest();
            test.start();
            userThreadTests.add(test);
        }
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userThreadTests.size(); i++) {
            UserThreadTest u = userThreadTests.get(i);
            u.join();
            users.add(u.getUser());
        }
        Thread.sleep(30000);
        users.stream().forEach(u->u.stopListening());
        Assert.assertEquals(true, checkMessages(users));
    }


    private boolean checkMessages(List<User> users) {
        boolean ok = true;
        List<Message> sentMessages = mapSentMessagesToMessages(users);
        List<Message> receivedMessages = mapReceivedMessagesToMessages(users);
        int inactive = 0;
        for (Message sent : sentMessages
        ) {
            boolean found = false;
            for (Message received : receivedMessages
            ) {
                if (received.getFrom().equals("Messaging Server") && received.getContent().equals("User not found!")) {
                    inactive++;
                    continue;
                }
                int compare = sent.compareTo(received);
                if (compare == 0) {
                    found = true;
                    break;
                }
            }
            ok = found;
            if (!ok) {
                System.out.println(sent.getTo());
                System.out.println(sent.getFrom());
                System.out.println(sent.getContent());
                break;
            }
        }
        System.out.println("Inactive users " + inactive);
        System.out.printf("Messages Sent: %d -> Messages Received %d \n", sentMessages.size(), receivedMessages.size());
        return ok;
    }

    private List<Message> mapSentMessagesToMessages(List<User> users) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            List<OutputMessage> userSent = users.get(i).getSentMessages().getAllSentMessages();
            for (int j = 0; j < userSent.size(); j++) {
                OutputMessage messageSending = userSent.get(j);
                messages.add(new Message(users.get(i).getName(), messageSending.getTo(), messageSending.getMessage()));
            }
        }
        return messages;
    }

    private List<Message> mapReceivedMessagesToMessages(List<User> users) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            List<InputMessage> userReceived = users.get(i).getInbox().getAllMessages();
            for (int j = 0; j < userReceived.size(); j++) {
                InputMessage messageReceive = userReceived.get(j);
                messages.add(new Message(messageReceive.getFrom(), users.get(i).getName(), messageReceive.getMessage()));
            }
        }
        return messages;
    }
}

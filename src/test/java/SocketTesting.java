import client.MessageReceiving;
import client.MessageSending;
import client.User;
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
import java.util.Random;

public class SocketTesting {

    private static MessagingManager manager;

    public SocketTesting() {
    }

    @BeforeClass
    public static void initialize() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Class<?> aClass = Class.forName("messaging.MessagingManager");
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        Constructor c = declaredConstructors[0];
        Object o = c.newInstance();
        manager = (MessagingManager) o;
        Runner runner = new Runner(o, null);
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
            User user1 = new User("Papi");
            User user2 = new User("Nick");
            String messageToSecond = "testTo " + user2.getName();
            String messageToFirst = "TestTo " + user1.getName();
            user1.sendMessage(user2.getName(), messageToSecond);
            user2.sendMessage(user1.getName(), messageToFirst);
            List<MessageReceiving> user2Inbox = user2.getInbox().getAllMessages();
            List<MessageReceiving> user1Inbox = user1.getInbox().getAllMessages();
            MessageReceiving testFromFirst = new MessageReceiving(user1.getName(), messageToSecond);
            MessageReceiving testFromSecond = new MessageReceiving(user2.getName(), messageToFirst);
            user1.stopListening();
            user2.stopListening();

            if (user2Inbox.get(0).equals(testFromFirst) && user1Inbox.get(0).equals(testFromSecond)) {
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
            User user1 = new User("Bumba");
            user1.sendMessage("nonExistent", "do you even exist?");
            List<MessageReceiving> user1Inbox = user1.getInbox().getAllMessages();
            MessageReceiving messageReceivingTest = new MessageReceiving("Messaging Server", "User not found!");
            user1.stopListening();
            boolean passed = false;
            if (user1Inbox.get(0).equals(messageReceivingTest)) {
                passed = true;
            }
            Assert.assertTrue("Fucked up!", passed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkMessages(List<MessageSending> tms, List<List<User>> users) {
        boolean ok = true;
        outer:for (MessageSending message: tms
             ) {
            String to = message.getTo();
            String messageStr = message.getMessage();
            for (List<User> usersList: users
                 ) {
                for (User user: usersList
                     ) {
                    if (user.getName().equals(to)){
                        List<MessageReceiving> inbox = user.getInbox().getAllMessages();
                        for (MessageReceiving messageReceiving: inbox
                             ) {
                            if (messageStr.equals(messageReceiving.getMessage())){
                                continue outer;
                            }
                        }
                        return false;
                    }
                }
            }
            return false;
        }
        return ok;
    }
}

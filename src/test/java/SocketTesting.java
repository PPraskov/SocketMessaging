import messaging.MessagingManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        Method initializeAndStart = aClass.getMethod("initializeAndStart");
        Runner runner = new Runner(o, initializeAndStart, null);
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
             TestUser user1 = new TestUser("Papi");
             user1.requireAuth();
             TestUser user2 = new TestUser("Nick");
             user2.requireAuth();

            String messageToSecond = "testToSecond";
            String messageToFirst = "TestToFirst";
            user1.sendMessage(user2.getName(), messageToSecond);
            user2.sendMessage(user1.getName(), messageToFirst);
            TestMessageReceiving fromFirst = user2.receiveMessage();
            TestMessageReceiving fromSecond = user1.receiveMessage();

            TestMessageReceiving testFromFirst = new TestMessageReceiving(user1.getName(), messageToSecond);
            TestMessageReceiving testFromSecond = new TestMessageReceiving(user2.getName(), messageToFirst);

            if (fromFirst.equals(testFromFirst) && fromSecond.equals(testFromSecond)) {
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
            TestUser user1 = new TestUser("Bumba");
            user1.requireAuth();
            user1.sendMessage("nonExistent", "do you even exist?");
            TestMessageReceiving messageReceiving = user1.receiveMessage();
            TestMessageReceiving messageReceivingTest = new TestMessageReceiving("Messaging Server", "User not found!");
            boolean passed = false;
            if (messageReceiving.equals(messageReceivingTest)) {
                passed = true;
            }
            Assert.assertTrue("Fucked up!", passed);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

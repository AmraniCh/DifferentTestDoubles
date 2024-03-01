import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class NewsletterSenderTest {

    @Test
    public void testDependencies()
    {
        // DUMMY
        SubscribersDatabase subscribersDatabase = new SubscribersDatabase();
        MessagingEngine messagingEngine = new MessagingEngine();

        NewsletterSender sender = new NewsletterSender(subscribersDatabase, messagingEngine);

        assertEquals(subscribersDatabase, sender.getSubscribersDatabase());
        assertEquals(messagingEngine, sender.getMessagingEngine());
    }

    @Test
    public void test_numberOfSubscribers()
    {
        // STUB
        SubscribersDatabase subscribersDatabaseMock =  mock(SubscribersDatabase.class);
        MessagingEngine messagingEngine = new MessagingEngine();

        NewsletterSender sender = new NewsletterSender(subscribersDatabaseMock, messagingEngine);

        List<String> subscribersList = Arrays.asList("email1", "email2", "email");
        when(subscribersDatabaseMock.getSubscribers()).thenReturn(subscribersList);

        assertEquals(3, sender.numberOfSubscribers());
    }

    @Test
    public void test_sendNewsletter_throws_zero_subscribers_exception()
    {
        SubscribersDatabase subscribersDatabaseMock =  mock(SubscribersDatabase.class);
        MessagingEngine messagingEngine = new MessagingEngine();

        NewsletterSender sender = new NewsletterSender(subscribersDatabaseMock, messagingEngine);

        when(subscribersDatabaseMock.getSubscribers()).thenReturn(Arrays.asList());

        Exception exception = assertThrows(ZeroSubscribersException.class, () -> sender.sendNewsletter("Hello there!"));
        assertEquals("you have zero subscriptions!", exception.getMessage());
    }

    @Test(expected = ZeroSubscribersException.class)
    public void test_sendNewsletter_throws_zero_subscribers_exception_2()
    {
        NewsletterSender sender = new NewsletterSender(new SubscribersDatabase(), new MessagingEngine());
        NewsletterSender senderSpy = spy(sender);

        when(senderSpy.numberOfSubscribers()).thenReturn(0);

        senderSpy.sendNewsletter("SUBJECT");
    }

    @Test
    public void test_sendNewsletter_success()
    {
        SubscribersDatabase subscribersDatabaseMock =  mock(SubscribersDatabase.class);
        MessagingEngine messagingEngineMock = mock(MessagingEngine.class);

        NewsletterSender sender = new NewsletterSender(subscribersDatabaseMock, messagingEngineMock);

        List<String> subscribersList = Arrays.asList("email1", "email2", "email");
        when(subscribersDatabaseMock.getSubscribers()).thenReturn(subscribersList);

        sender.sendNewsletter("Hello!");

        verify(messagingEngineMock, times(1)).sendEmail("Hello!", subscribersList);
    }
}

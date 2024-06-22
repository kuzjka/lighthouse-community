package ua.org.meters.lighthouse.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.org.meters.lighthouse.service.PowerEvent;

import static org.mockito.Mockito.*;

public class NotificationManagerTest {
    private PowerNotifier firstNotifier;
    private PowerNotifier secondNotifier;
    private NotificationManager service;

    @BeforeEach
    public void setUp() {
        firstNotifier = mock(PowerNotifier.class);
        secondNotifier = mock(PowerNotifier.class);
        service = new NotificationManager();
        service.addPowerNotifier(firstNotifier);
        service.addPowerNotifier(secondNotifier);
    }

    @Test
    public void testNotification() {
        service.onPowerEvent(new PowerEvent(null, true));
        verify(firstNotifier).notifyPower(true);
        verify(secondNotifier).notifyPower(true);
    }

    @Test
    public void testPowerOffNotification() {
        service.onPowerEvent(new PowerEvent(null, false));
        verify(firstNotifier).notifyPower(false);
        verify(secondNotifier).notifyPower(false);
    }
}

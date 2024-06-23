package ua.org.meters.lighthouse.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import ua.org.meters.lighthouse.service.PowerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to power state change events, chooses appropriate notification channels and send notification to users.
 */
public class NotificationManager {
    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    private List<PowerNotifier> powerNotifiers = new ArrayList<>();

    /**
     * Adds a notifier for power events.
     * @param notifier  Power event notifier
     */
    public void addPowerNotifier(PowerNotifier notifier) {
        powerNotifiers.add(notifier);
    }

    @EventListener
    public void onPowerEvent(PowerEvent event) {
        powerNotifiers.forEach(notifier -> {
            try {
                notifier.notifyPower(event.isPowerOn());
            } catch (NotificationException e) {
                logger.error("Notification failed", e);
            }
        });
    }
}

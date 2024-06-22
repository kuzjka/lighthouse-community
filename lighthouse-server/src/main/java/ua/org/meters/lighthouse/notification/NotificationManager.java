package ua.org.meters.lighthouse.notification;

import org.springframework.context.event.EventListener;
import ua.org.meters.lighthouse.service.PowerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to power state change events, chooses appropriate notification channels and send notification to users.
 */
public class NotificationManager {
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
        powerNotifiers.forEach(notifier -> notifier.notifyPower(event.isPowerOn()));
    }
}

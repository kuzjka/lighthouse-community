package ua.org.meters.lighthouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.org.meters.lighthouse.notification.FCMNotifier;
import ua.org.meters.lighthouse.notification.LoggingNotifier;
import ua.org.meters.lighthouse.notification.NotificationManager;

import java.time.Clock;

@Configuration
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public NotificationManager notificationManager(FCMNotifier fcmNotifier) {
        NotificationManager manager = new NotificationManager();
        manager.addPowerNotifier(new LoggingNotifier());
        manager.addPowerNotifier(fcmNotifier);
        return manager;
    }
}

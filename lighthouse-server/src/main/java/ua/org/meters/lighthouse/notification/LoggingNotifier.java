package ua.org.meters.lighthouse.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNotifier implements PowerNotifier {
    private static final Logger logger = LoggerFactory.getLogger(PowerNotifier.class);

    @Override
    public void notifyPower(boolean powerOn) {
        logger.info("Power event: power is {}", powerOn ? "on" : "off");
    }
}

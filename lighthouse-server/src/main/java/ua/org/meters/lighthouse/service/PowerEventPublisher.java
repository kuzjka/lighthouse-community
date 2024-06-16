package ua.org.meters.lighthouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PowerEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishPowerEvent(final boolean powerOn) {
        System.out.println("Publishing power event. ");
        PowerEvent powerEvent = new PowerEvent(this, powerOn);
        applicationEventPublisher.publishEvent(powerEvent);
    }
}

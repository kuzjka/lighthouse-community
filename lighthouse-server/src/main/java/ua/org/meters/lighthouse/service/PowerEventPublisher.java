package ua.org.meters.lighthouse.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PowerEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public PowerEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishPowerEvent(final boolean powerOn) {
        System.out.println("Publishing power event...");
        PowerEvent powerEvent = new PowerEvent(this, powerOn);
        applicationEventPublisher.publishEvent(powerEvent);
    }
}

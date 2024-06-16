package ua.org.meters.lighthouse.service;

import org.springframework.context.ApplicationEvent;

public class PowerEvent extends ApplicationEvent {
    private boolean powerOn;

    public PowerEvent(Object source, boolean powerOn) {
        super(source);
        this.powerOn = powerOn;
    }

    public boolean isPowerOn() {
        return powerOn;
    }
}

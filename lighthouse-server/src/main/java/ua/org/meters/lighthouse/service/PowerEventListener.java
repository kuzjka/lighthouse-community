package ua.org.meters.lighthouse.service;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PowerEventListener implements ApplicationListener<PowerEvent> {

    @Override
    public void onApplicationEvent(PowerEvent event) {
        System.out.println("powerOn: " + event.isPowerOn());
    }
}


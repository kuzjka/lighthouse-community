package ua.org.meters.lighthouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class SensorService {
    private boolean sensorState;
    private Calendar lastReport = Calendar.getInstance();

    @Autowired
    PowerEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 30000)
    public void checkSensor() {
        System.out.println("checkSensorState()");
        if (Calendar.getInstance().getTimeInMillis() - lastReport.getTimeInMillis() > 60000) {
            this.sensorState = false;
        } else {
            this.sensorState = true;
        }
        System.out.println("power on: " + this.sensorState);
    }

    @Scheduled(fixedDelay = 120000)
    public void onSensorReport() {
        System.out.println("onSensorReport()");
        this.sensorState = true;
        this.lastReport = Calendar.getInstance();
        this.eventPublisher.publishPowerEvent(this.sensorState);
    }
}

package ua.org.meters.lighthouse.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SensorService {
    private boolean sensorState;
    private Instant lastReport;

    private PowerEventPublisher eventPublisher;

    public SensorService(PowerEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.sensorState = true;
        this.lastReport = Instant.now();
    }

    @Scheduled(fixedDelay = 30000)
    public void checkSensor() {
        System.out.println("checkSensorState()");
        if ((Instant.now().getEpochSecond() - lastReport.getEpochSecond()) >= 60 && this.sensorState) {
            this.sensorState = false;
            this.eventPublisher.publishPowerEvent(false);
        }
    }

    @Scheduled(fixedDelay = 120000)
    public void onSensorReport() {
        System.out.println("onSensorReport()");
        if (!this.sensorState) {
            this.lastReport = Instant.now();
            this.sensorState = true;
            this.eventPublisher.publishPowerEvent(true);
        }
    }
}

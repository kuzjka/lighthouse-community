package ua.org.meters.lighthouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Keeps the state of the sensor and emits {@link PowerEvent} if the state of power sensor changes.
 */
@Service
public class SensorService {
    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);

    private boolean sensorState;
    private Instant lastReport;

    private PowerEventPublisher eventPublisher;
    private Clock clock;
    private int timeout;

    /**
     * Creates a new service instance.
     * @param eventPublisher    Power event publisher
     * @param timeout           Time in seconds without reports before the sensor is considered unpowered
     * @param clock             Clock instance
     */
    @Autowired
    public SensorService(PowerEventPublisher eventPublisher,
                         @Value("${lighthouse.sensor.timeout}") int timeout,
                         Clock clock) {
        this(eventPublisher, timeout, clock, true);
    }

    /**
     * Creates a new service instance with initial state.
     * @param eventPublisher    Power event publisher
     * @param timeout           Time in seconds without reports before the sensor is considered unpowered
     * @param clock             Clock instance
     * @param initialState      Initial power state: {@code true} - powered, {@code false} - unpowered
     */
    SensorService(PowerEventPublisher eventPublisher, int timeout, Clock clock, boolean initialState) {
        this.eventPublisher = eventPublisher;
        this.timeout = timeout;
        this.clock = clock;
        this.sensorState = initialState;
        this.lastReport = Instant.now(clock);
    }

    /**
     * Checks the state opf the sensor and emits an event if the state is changed.
     * This method is scheduled.
     */
    @Scheduled(fixedDelayString = "${lighthouse.sensor.checkRate}", timeUnit = TimeUnit.SECONDS)
    public void checkSensor() {
        logger.debug("checkSensorState()");
        if ((Instant.now(clock).getEpochSecond() - lastReport.getEpochSecond()) >= timeout && this.sensorState) {
            this.sensorState = false;
            this.eventPublisher.publishPowerEvent(false);
        }
    }

    /**
     * Called when received report from the sensor.
     */
    public void onSensorReport() {
        logger.debug("onSensorReport()");
        if (!this.sensorState) {
            this.lastReport = Instant.now(clock);
            this.sensorState = true;
            this.eventPublisher.publishPowerEvent(true);
        }
    }
}

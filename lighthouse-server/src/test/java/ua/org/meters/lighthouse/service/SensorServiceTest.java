package ua.org.meters.lighthouse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;

import static org.mockito.Mockito.*;


public class SensorServiceTest {
    private PowerEventPublisher eventPublisher;
    private Clock clock;

    @BeforeEach
    public void setUp() {
        eventPublisher = mock(PowerEventPublisher.class);
        clock = mock(Clock.class);
    }

    @Test
    public void testPowerOffTrue() {
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        SensorService service = new SensorService(eventPublisher, 60, clock, true);

        /* assume 70 seconds passed */
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:05:10Z"));
        service.checkSensor();

        /* should emit power off event */
        verify(eventPublisher).publishPowerEvent(false);
    }
    @Test
    public void testPowerOffFalse() {
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        SensorService service = new SensorService(eventPublisher, 60, clock, true);

        /* assume 50 seconds passed */
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:50Z"));
        service.checkSensor();

        /* should not emit power off event */
        verifyNoInteractions(eventPublisher);
    }
    @Test
    public void testPowerOnTrue() {
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        SensorService service = new SensorService(eventPublisher, 60, clock, false);

        /* assume 0 seconds passed */
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        service.onSensorReport();

        /* should emit power on event */
        verify(eventPublisher).publishPowerEvent(true);
    }

    @Test
    public void testPowerOnFalse() {
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        SensorService service = new SensorService(eventPublisher, 60, clock, true);

        /* assume 0 seconds passed */
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        service.onSensorReport();

        /* should not emit power on event */
        verifyNoInteractions(eventPublisher);
    }
}

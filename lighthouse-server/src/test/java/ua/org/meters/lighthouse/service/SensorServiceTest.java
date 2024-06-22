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
    public void testPowerOff() {
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:04:00Z"));
        SensorService service = new SensorService(eventPublisher, 60, clock, true);

        /* assume 70 seconds passed */
        when(clock.instant()).thenReturn(Instant.parse("2024-06-22T17:05:10Z"));
        service.checkSensor();

        /* should emit power off event */
        verify(eventPublisher).publishPowerEvent(false);
    }
}

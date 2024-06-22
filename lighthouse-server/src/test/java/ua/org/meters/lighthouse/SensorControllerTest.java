package ua.org.meters.lighthouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import ua.org.meters.lighthouse.config.SecurityConfig;
import ua.org.meters.lighthouse.service.SensorService;
import ua.org.meters.lighthouse.web.SensorController;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@WebMvcTest(SensorController.class)
@Import(SecurityConfig.class)
public class SensorControllerTest {
    @MockBean
    private SensorService service;

    @Test
    @WithMockUser
    public void testReport(@Autowired WebTestClient client) {
        client.post().uri("/api/sensor/report")
                .exchange()
                .expectStatus().isOk();

        verify(service).onSensorReport();
    }

    @Test
    public void testReportUnauthorized(@Autowired WebTestClient client) {
        client.post().uri("/api/sensor/report")
                .exchange()
                .expectStatus().isUnauthorized();

        verify(service, never()).onSensorReport();
    }
}

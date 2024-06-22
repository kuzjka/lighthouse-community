package ua.org.meters.lighthouse.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.org.meters.lighthouse.service.SensorService;

@RestController
public class SensorController {
    @Autowired
    private SensorService sensorService;

    @PostMapping("/api/sensor/report")
    public void report() {
        sensorService.onSensorReport();
    }
}

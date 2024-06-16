package ua.org.meters.lighthouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LighthouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(LighthouseApplication.class, args);
	}

}

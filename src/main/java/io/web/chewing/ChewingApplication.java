package io.web.chewing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJdbcAuditing
@EnableScheduling
public class ChewingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChewingApplication.class, args);
    }

}

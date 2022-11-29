package io.web.chewing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@SpringBootApplication
@EnableJdbcAuditing
public class ChewingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChewingApplication.class, args);
    }

}

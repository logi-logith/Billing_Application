package io.github.logith.billing_application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingApplication {

    private static final Logger log = LoggerFactory.getLogger(BillingApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(BillingApplication.class, args);
        log.info("Application is Running");
    }

}

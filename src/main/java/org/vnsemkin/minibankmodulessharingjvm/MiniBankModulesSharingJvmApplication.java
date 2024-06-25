package org.vnsemkin.minibankmodulessharingjvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.vnsemkin.gbpbackend.GbpBackendApplication;
import org.vnsemkin.semkinmiddleservice.SemkinMiddleServiceApplication;
import org.vnsemkin.semkintelegrambot.SemkinTelegramBotApplication;

@SpringBootApplication
public class MiniBankModulesSharingJvmApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{GbpBackendApplication.class,
            SemkinMiddleServiceApplication.class,
            SemkinTelegramBotApplication.class},
            args);
    }
}

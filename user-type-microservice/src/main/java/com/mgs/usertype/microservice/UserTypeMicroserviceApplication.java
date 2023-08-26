package com.mgs.usertype.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mgs.*")
@EntityScan("com.mgs.*")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class UserTypeMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserTypeMicroserviceApplication.class, args);
    }

}

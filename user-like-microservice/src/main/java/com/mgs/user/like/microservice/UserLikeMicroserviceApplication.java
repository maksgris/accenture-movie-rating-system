package com.mgs.user.like.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.mgs.user.like.*", "com.mgs.library.*"})
@EntityScan({"com.mgs.*", "com.mgs.library.*"})
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class UserLikeMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserLikeMicroserviceApplication.class, args);
    }

}

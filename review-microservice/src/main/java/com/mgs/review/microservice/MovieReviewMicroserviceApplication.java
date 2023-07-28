package com.mgs.review.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.mgs.review.*", "com.mgs.library.*"})
@EntityScan({"com.mgs.*", "com.mgs.library.*"})
public class MovieReviewMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieReviewMicroserviceApplication.class, args);
    }

}

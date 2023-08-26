package com.mgs.movie.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.mgs.movie.*", "com.mgs.library.*"})
@EntityScan({"com.mgs.*", "com.mgs.library.*"})
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class MovieMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieMicroserviceApplication.class, args);
    }

}

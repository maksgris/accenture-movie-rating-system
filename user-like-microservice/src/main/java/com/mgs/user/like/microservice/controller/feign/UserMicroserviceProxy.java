package com.mgs.user.like.microservice.controller.feign;

import com.mgs.library.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(name = "user-microservice", url = "http://localhost:8300")
public interface UserMicroserviceProxy {

    @GetMapping("/api/v1/user/{userId}")
    public Optional<UserDTO> getUser(@PathVariable Long userId);
}

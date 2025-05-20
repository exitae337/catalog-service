package ru.example.catalogservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.example.catalogservice.feign")
public class FeignClientConfig {
}

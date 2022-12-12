package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Eurekaclient01Application {

    public static void main(String[] args) {
        SpringApplication.run(Eurekaclient01Application.class, args);
    }

}

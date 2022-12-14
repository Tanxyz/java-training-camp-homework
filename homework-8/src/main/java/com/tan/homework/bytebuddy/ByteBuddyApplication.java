package com.tan.homework.bytebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ByteBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ByteBuddyApplication.class);
    }

}

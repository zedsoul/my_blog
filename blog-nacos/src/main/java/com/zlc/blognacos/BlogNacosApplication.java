package com.zlc.blognacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class BlogNacosApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(BlogNacosApplication.class, args);


    }


}

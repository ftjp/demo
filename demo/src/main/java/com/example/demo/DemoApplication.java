package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * description: 启动类
 *
 * @author: LJP
 * @date: 2024/11/14 9:32
 */
@SpringBootApplication(scanBasePackages = {"com.example.*"})
@EnableAsync
@PropertySource("classpath:constants.properties")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

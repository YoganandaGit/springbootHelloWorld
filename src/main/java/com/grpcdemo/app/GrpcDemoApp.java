package com.grpcdemo.app;

import com.grpcdemo.app.service.MyDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GrpcDemoApp {

    @Autowired
    private MyDemoService myDemoService;

    @GetMapping("/hello")
    public String message() {
        return "Welcome to Minikube Hello world example";
    }

    @GetMapping("/demo")
    public String demoMessage() {
        return myDemoService.getDemoMessage();
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(GrpcDemoApp.class, args);
    }

}

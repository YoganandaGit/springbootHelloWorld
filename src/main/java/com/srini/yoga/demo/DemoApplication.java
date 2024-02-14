package com.srini.yoga.demo;

import com.srini.yoga.demo.service.MyDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

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

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

package com.grpcdemo.app.service;

import org.springframework.stereotype.Service;

@Service("myDemoService")
public class MyDemoService {

    public String getDemoMessage() {
        return "From main demo service method.";
    }

}

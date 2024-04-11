package com.grpcdemo.app.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OverridenMyDemoService extends MyDemoService{

        @Override
        public String getDemoMessage() {
            return "From overriden demo service method.";
        }
}

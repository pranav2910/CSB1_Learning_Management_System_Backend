package com.example.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SampleController {

    @GetMapping("/hello")
    public Map<String, Object> sayHello() { 
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Hello, World!");
        result.put("serverTime", System.currentTimeMillis());
        return result;
    }
    
}
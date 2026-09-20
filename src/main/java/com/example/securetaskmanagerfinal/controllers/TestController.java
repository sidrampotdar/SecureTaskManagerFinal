package com.example.securetaskmanagerfinal.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "Hello, Your Spring App is running successfully! ";
    }
}

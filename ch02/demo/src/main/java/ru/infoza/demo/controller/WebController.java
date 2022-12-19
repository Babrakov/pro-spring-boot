package ru.infoza.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @GetMapping
    public String index(){
        return "Hello Spring Boot";
    }
}

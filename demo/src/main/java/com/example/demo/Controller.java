package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class Controller {
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World";
    }

    @GetMapping("/bye")
    public String getBye() {
        return "Bye World";
    }
    
}

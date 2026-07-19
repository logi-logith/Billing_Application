package io.github.logith.billing_application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Welcome")
public class WelcomeController {
    @GetMapping
    public void welcome(){
        System.out.println("Welcome");
    }
//    @PostMapping
//    public void createUser(){
//
//    }
}

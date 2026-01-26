package io.github.logith.billing_application.Controller;

import io.github.logith.billing_application.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

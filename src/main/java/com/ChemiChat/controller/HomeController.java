package com.ChemiChat.controller;

import com.ChemiChat.ChemiChatApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/")
    public String home(Principal principal){
        return "Hello, " + principal.getName();
    }

    @PostMapping("/send")
    public void send(@RequestParam(value = "mess") String mess){
        rabbitTemplate.convertAndSend(ChemiChatApplication.topicExchangeName, "foo.bar.baz", mess);
    }

}

package com.ChemiChat.controller;

import com.ChemiChat.dto.ApiResponse;
import com.ChemiChat.dto.ApiResponseSingleOk;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
    @GetMapping("/user")
    public ResponseEntity<String> getUsername(Principal principal){
        return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
    }
}

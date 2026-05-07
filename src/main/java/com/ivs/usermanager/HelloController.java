package com.ivs.usermanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
    @GetMapping("/print")
    public String printText (){
        return "Vi Sexy";
    }
}
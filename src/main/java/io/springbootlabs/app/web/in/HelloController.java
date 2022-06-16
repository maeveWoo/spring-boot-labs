package io.springbootlabs.app.web.in;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {
    @GetMapping("/")
    public String hello(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "content/login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "index";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
}

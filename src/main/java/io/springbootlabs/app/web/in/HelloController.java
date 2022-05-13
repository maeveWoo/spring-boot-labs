package io.springbootlabs.app.web.in;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("/")
    public String hello(){
        return "index";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
}

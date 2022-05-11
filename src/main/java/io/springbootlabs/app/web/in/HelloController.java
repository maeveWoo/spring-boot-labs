package io.springbootlabs.app.web.in;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HelloController {
    @GetMapping
    public String hello(){
        return "index";
    }
}

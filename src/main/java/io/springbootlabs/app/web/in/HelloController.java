package io.springbootlabs.app.web.in;

import io.springbootlabs.app.web.dto.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HelloController {
    @GetMapping("/")
    public String hello(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/greeting")
    public String greeting(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@Valid @ModelAttribute Greeting greeting, BindingResult bindingResult, Model model) {
        model.addAttribute("greeting", greeting);
        if (bindingResult.hasErrors()) {
            return "greeting";
        }
        return "result";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
}

package io.springbootlabs.app.web.in;

import io.springbootlabs.domain.usecase.FileHandling;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileHandling fileHandling;

    @GetMapping("/image")
    public ModelAndView getImage(Principal principal) {
        Map<String, Object> map = new HashMap<>();
        map.put("images", fileHandling.findListByUser(Long.parseLong(principal.getName())));
        return new ModelAndView("content/file/image", map);
    }

    @GetMapping("/bmp")
    public String getBmp() {
        return "content/file/bmp";
    }

    @GetMapping("/mp3")
    public String getMp3() {
        return "content/file/mp3";
    }
}

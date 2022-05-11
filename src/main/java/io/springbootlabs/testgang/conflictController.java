package io.springbootlabs.testgang;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class conflictController {
    private final ApplicationContext context;

//    @GetMapping
    public String conflict() {
        /*
        org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'requestMappingHandlerMapping' defined in class path resource [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class]: Invocation of init method failed; nested exception is java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'conflictController' method
        io.springbootlabs.testgang.conflictController#conflict()
        to {GET []}: There is already 'helloController' bean method
         */
        System.out.println(">>>" + context.getBean(conflictController.class));

        return "index";
    }

    @GetMapping("!Captitalize")
    public String conflict2() {
        // TODO ????????????
        System.out.println(">>> " + context.getBean(conflictController.class));
        return "index";
    }
}

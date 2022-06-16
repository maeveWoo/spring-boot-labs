package io.springbootlabs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootLabsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLabsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
            for (String name : beanDefinitionNames) {
                System.out.println("> " + name);
            }
        };
    }
}

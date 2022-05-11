package io.springbootlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootLabsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootLabsApplication.class, args);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name: beanDefinitionNames) {
            System.out.println("bean name: "+ name);
        }
    }

}

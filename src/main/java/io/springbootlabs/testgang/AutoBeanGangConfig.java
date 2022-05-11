package io.springbootlabs.testgang;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoBeanGangConfig {
    @Bean
    public AutoBeanGang autoBeanGang() {return new AutoBeanGang();}
}

package io.springbootlabs;

import io.springbootlabs.app.web.in.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class SpringBootLabsApplicationTest {

    @Autowired
    private HelloController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
    @Test
    public void test() {
        assertThat("냥").isEqualTo("냥");
    }
}
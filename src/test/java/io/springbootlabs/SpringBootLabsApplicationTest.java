package io.springbootlabs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class SpringBootLabsApplicationTest {
    @Test
    public void test() {
        assertThat("냥").isEqualTo("냥");
    }
}
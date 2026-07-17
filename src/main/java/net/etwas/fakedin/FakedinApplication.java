package net.etwas.fakedin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FakedinApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakedinApplication.class, args);
    }

}

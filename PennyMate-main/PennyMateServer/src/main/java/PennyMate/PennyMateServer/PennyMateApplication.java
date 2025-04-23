package PennyMate.PennyMateServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "PennyMate")  // 👈 Scansiona TUTTO sotto PennyMate
public class PennyMateApplication {
    public static void main(String[] args) {
        SpringApplication.run(PennyMateApplication.class, args);
    }
}

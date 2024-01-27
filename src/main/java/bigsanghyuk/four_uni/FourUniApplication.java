package bigsanghyuk.four_uni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FourUniApplication {

    public static void main(String[] args) {
        SpringApplication.run(FourUniApplication.class, args);
    }

}

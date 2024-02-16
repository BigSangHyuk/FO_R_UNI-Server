package bigsanghyuk.four_uni;

import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing
public class FourUniApplication {

    public static void main(String[] args) {
        SpringApplication.run(FourUniApplication.class, args);
    }

    @Bean
    public ExampleData testDataInit(UserRepository userRepository, PostRepository postRepository, PasswordEncoder encoder) {
        return new ExampleData(userRepository, postRepository, encoder);
    }

}

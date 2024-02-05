package bigsanghyuk.four_uni.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@OpenAPIDefinition(info = @Info(title = "FO_R UNI", description = "FO_R UNI APP API 명세", version = "v1"))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openApi() {
        String[] paths = {"/**"};
        return GroupedOpenApi.builder()
                .group("API V1")
                .pathsToMatch(paths)
                .build();
    }
}

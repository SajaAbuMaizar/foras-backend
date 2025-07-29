package portal.forasbackend.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Foras Job Portal API")
                        .version("1.0.0")
                        .description("RESTful API for Foras job portal supporting Arabic/Hebrew languages")
                        .contact(new Contact()
                                .name("Foras Team")
                                .email("support@foras.co.il")
                                .url("https://foras.co.il"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://foras.co.il/terms")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("https://api.foras.co.il").description("Production")
                ))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token from cookie")));
    }
}
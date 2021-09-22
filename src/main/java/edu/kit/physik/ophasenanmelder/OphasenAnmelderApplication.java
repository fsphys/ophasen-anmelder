package edu.kit.physik.ophasenanmelder;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OphasenAnmelderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OphasenAnmelderApplication.class, args);
    }

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("O-Phasen Anmelder API Definition").version(OphasenAnmelderApplication.class.getPackage().getImplementationVersion()));
    }
}

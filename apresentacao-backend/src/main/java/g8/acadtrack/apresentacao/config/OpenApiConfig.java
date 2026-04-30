package g8.acadtrack.apresentacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI acadTrackOpenApi(@Value("${server.port:8080}") int serverPort) {
        String localBaseUrl = "http://localhost:" + serverPort;
        return new OpenAPI()
                .info(new Info()
                        .title("AcadTrack API")
                        .version("1.0-SNAPSHOT")
                        .description("API REST do AcadTrack (turmas, alunos, responsáveis, simulados, notas e retificações).\n\n"
                                + "**Try it out**: o servidor **`"
                                + localBaseUrl
                                + "`** corresponde ao `server.port` deste processo.")
                        .contact(new Contact().name("Projeto AcadTrack")))
                .servers(List.of(
                        new Server().url("/").description("Mesma origem do Swagger UI (evita porta errada)"),
                        new Server().url(localBaseUrl).description("Mesma porta do backend (`server.port=" + serverPort + "`)")
                ));
    }
}
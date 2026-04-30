package g8.acadtrack.bdd;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "g8.acadtrack")
@EnableJpaRepositories(basePackages = "g8.acadtrack.infraestrutura.persistencia.springdata")
@EntityScan(basePackages = "g8.acadtrack.infraestrutura.persistencia.entidade")
public class TestSpringConfiguration {
}
package br.com.acadtrack.bdd;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "br.com.acadtrack")
@EnableJpaRepositories(basePackages = "br.com.acadtrack.infraestrutura.persistencia.springdata")
@EntityScan(basePackages = "br.com.acadtrack.infraestrutura.persistencia.entidade")
public class TestSpringConfiguration {
}
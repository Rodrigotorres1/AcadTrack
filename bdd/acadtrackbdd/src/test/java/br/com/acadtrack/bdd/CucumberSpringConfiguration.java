package br.com.acadtrack.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TestSpringConfiguration.class)
public class CucumberSpringConfiguration {
}
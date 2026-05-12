package g8.acadtrack.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@CucumberContextConfiguration
@SpringBootTest(classes = TestSpringConfiguration.class)
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}

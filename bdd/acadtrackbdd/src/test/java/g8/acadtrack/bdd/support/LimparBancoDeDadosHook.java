package g8.acadtrack.bdd.support;

import io.cucumber.java.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class LimparBancoDeDadosHook {

    private final JdbcTemplate jdbcTemplate;
    private final TestContext testContext;

    public LimparBancoDeDadosHook(JdbcTemplate jdbcTemplate, TestContext testContext) {
        this.jdbcTemplate = jdbcTemplate;
        this.testContext = testContext;
    }

    @Before
    public void limparDadosAntesDeExemplo() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM notificacao_responsavel");
        jdbcTemplate.execute("DELETE FROM solicitacao_retificacao");
        jdbcTemplate.execute("DELETE FROM nota");
        jdbcTemplate.execute("DELETE FROM simulado_disciplina");
        jdbcTemplate.execute("DELETE FROM simulado");
        jdbcTemplate.execute("DELETE FROM aluno");
        jdbcTemplate.execute("DELETE FROM responsavel");
        jdbcTemplate.execute("DELETE FROM turma");
        jdbcTemplate.execute("DELETE FROM disciplina");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        testContext.resetMensagens();
    }
}

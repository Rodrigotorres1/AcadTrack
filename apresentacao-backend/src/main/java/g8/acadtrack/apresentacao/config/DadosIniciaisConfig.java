package g8.acadtrack.apresentacao.config;

import g8.acadtrack.aplicacao.turma.CriarTurmaUseCase;
import g8.acadtrack.aplicacao.turma.LimparTurmasDuplicadasUseCase;
import g8.acadtrack.aplicacao.turma.ListarTurmasUseCase;
import g8.acadtrack.dominioacademico.turma.Turma;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DadosIniciaisConfig {

    private static final List<String> TURMAS_PADRAO = List.of(
            "1\u00BA A",
            "1\u00BAB",
            "2\u00BAA",
            "2\u00BAB",
            "3\u00BAA",
            "3\u00BAB"
    );

    @Bean
    CommandLineRunner criarTurmasPadrao(
            ListarTurmasUseCase listarTurmasUseCase,
            CriarTurmaUseCase criarTurmaUseCase,
            LimparTurmasDuplicadasUseCase limparTurmasDuplicadasUseCase,
            JdbcTemplate jdbcTemplate
    ) {
        return args -> {
            limparTurmasDuplicadasUseCase.executar();

            Set<String> turmasExistentes = listarTurmasUseCase.executar()
                    .stream()
                    .map(Turma::getNomeNormalizado)
                    .collect(Collectors.toSet());

            TURMAS_PADRAO.stream()
                    .filter(nome -> !turmasExistentes.contains(Turma.normalizarNome(nome)))
                    .forEach(criarTurmaUseCase::executar);

            ajustarIdsSequenciaisH2(jdbcTemplate);
        };
    }

    private void ajustarIdsSequenciaisH2(JdbcTemplate jdbcTemplate) {
        List<String> tabelasComIdentity = jdbcTemplate.queryForList("""
                SELECT TABLE_NAME
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = 'PUBLIC'
                  AND COLUMN_NAME = 'ID'
                  AND IS_IDENTITY = 'YES'
                """, String.class);

        for (String tabela : tabelasComIdentity) {
            String nomeTabela = quoteIdentifier(tabela);
            jdbcTemplate.execute("ALTER TABLE " + nomeTabela + " ALTER COLUMN \"ID\" SET NO CACHE");

            Number proximoId = jdbcTemplate.queryForObject(
                    "SELECT COALESCE(MAX(\"ID\"), 0) + 1 FROM " + nomeTabela,
                    Number.class
            );
            long proximoValor = (proximoId != null) ? proximoId.longValue() : 1L;
            jdbcTemplate.execute("ALTER TABLE " + nomeTabela + " ALTER COLUMN \"ID\" RESTART WITH " + proximoValor);
        }
    }

    private String quoteIdentifier(String identifier) {
        return "\"" + identifier.replace("\"", "\"\"") + "\"";
    }
}

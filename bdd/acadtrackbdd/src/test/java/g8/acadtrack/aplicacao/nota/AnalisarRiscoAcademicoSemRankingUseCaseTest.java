package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.aplicacao.nota.risco.RiscoAltoStrategy;
import g8.acadtrack.aplicacao.nota.risco.RiscoBaixoStrategy;
import g8.acadtrack.aplicacao.nota.risco.RiscoModeradoStrategy;
import g8.acadtrack.aplicacao.ranking.ContadorParticipantesRankingPort;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class AnalisarRiscoAcademicoSemRankingUseCaseTest {

    @Test
    void deveDesabilitarHookDeRanking() {
        AnalisarRiscoAcademicoSemRankingUseCase useCase = criarUseCaseComMocks();

        assertFalse(useCase.incluirRanking());
    }

    @Test
    void deveExecutarFluxoSemCalcularRanking() {
        NotaRepository notaRepository = mock(NotaRepository.class);
        AlunoRepository alunoRepository = mock(AlunoRepository.class);
        SimuladoRepository simuladoRepository = mock(SimuladoRepository.class);
        DisciplinaRepository disciplinaRepository = mock(DisciplinaRepository.class);
        ContadorParticipantesRankingPort contadorParticipantesRankingPort =
                mock(ContadorParticipantesRankingPort.class);

        AnalisarRiscoAcademicoSemRankingUseCase useCase = criarUseCase(
                notaRepository,
                alunoRepository,
                simuladoRepository,
                disciplinaRepository,
                contadorParticipantesRankingPort
        );

        Long alunoId = 100L;
        when(alunoRepository.buscarPorId(alunoId)).thenReturn(Optional.of(
                new Aluno(alunoId, "Ana Lima", "ana.lima@email.com", null, null)
        ));
        when(notaRepository.buscarPorAlunoId(alunoId)).thenReturn(List.of(
                new Nota(1L, alunoId, 10L, 1L, 4.0),
                new Nota(2L, alunoId, 10L, 2L, 5.0),
                new Nota(3L, alunoId, 20L, 1L, 4.0),
                new Nota(4L, alunoId, 20L, 2L, 5.0)
        ));
        when(simuladoRepository.buscarPorIds(anyList())).thenReturn(List.of(
                new Simulado(10L, "Simulado 1"),
                new Simulado(20L, "Simulado 2")
        ));
        when(simuladoRepository.buscarPesosDisciplinasPorSimuladoIds(anyList())).thenReturn(List.of(
                new SimuladoDisciplina(1L, 10L, 1L, 1.0),
                new SimuladoDisciplina(2L, 10L, 2L, 1.0),
                new SimuladoDisciplina(3L, 20L, 1L, 1.0),
                new SimuladoDisciplina(4L, 20L, 2L, 1.0)
        ));
        when(disciplinaRepository.buscarPorIds(anyList())).thenReturn(List.of(
                new Disciplina(1L, "Matematica"),
                new Disciplina(2L, "Portugues")
        ));

        AnaliseDesempenhoAcademicoResultado resultado = useCase.executar(alunoId);

        assertEquals(4.5, resultado.mediaGeral());
        assertEquals(SituacaoAcademica.REPROVADO, resultado.situacaoAcademica());
        assertEquals(NivelRiscoAcademico.ALTO, resultado.nivelRisco());
        assertTrue(resultado.riscoAcademico());
        assertNull(resultado.posicaoRanking());
        assertEquals(0, resultado.totalAlunosRanking());
        assertFalse(resultado.alunoNoTop10());
        assertEquals("Ranking calculado sob demanda na consulta de desempenho", resultado.mensagemRanking());
        assertEquals(2, resultado.totalSimulados());
        assertEquals(2, resultado.simuladosComBaixoDesempenho());
        verifyNoInteractions(contadorParticipantesRankingPort);
    }

    private AnalisarRiscoAcademicoSemRankingUseCase criarUseCaseComMocks() {
        return criarUseCase(
                mock(NotaRepository.class),
                mock(AlunoRepository.class),
                mock(SimuladoRepository.class),
                mock(DisciplinaRepository.class),
                mock(ContadorParticipantesRankingPort.class)
        );
    }

    private AnalisarRiscoAcademicoSemRankingUseCase criarUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            ContadorParticipantesRankingPort contadorParticipantesRankingPort
    ) {
        return new AnalisarRiscoAcademicoSemRankingUseCase(
                notaRepository,
                alunoRepository,
                new AvaliacaoAcademicaService(),
                simuladoRepository,
                disciplinaRepository,
                contadorParticipantesRankingPort,
                new ClassificadorRiscoAcademicoService(List.of(
                        new RiscoAltoStrategy(),
                        new RiscoModeradoStrategy(),
                        new RiscoBaixoStrategy()
                ))
        );
    }
}

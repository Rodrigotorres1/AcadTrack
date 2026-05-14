package g8.acadtrack.bdd.unit;

import g8.acadtrack.aplicacao.retificacao.ListarRetificacoesUseCase;
import g8.acadtrack.aplicacao.retificacao.MontarDetalheRetificacaoService;
import g8.acadtrack.aplicacao.retificacao.SolicitacaoRetificacaoDetalheResultado;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominioavaliacao.retificacao.StatusSolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListarRetificacoesUseCaseTest {

    @Test
    void deveMontarDetalhesEmLoteSemBuscarRelacionamentosPorItem() {
        SolicitacaoRetificacaoRepository solicitacaoRepository = mock(SolicitacaoRetificacaoRepository.class);
        NotaRepository notaRepository = mock(NotaRepository.class);
        AlunoRepository alunoRepository = mock(AlunoRepository.class);
        DisciplinaRepository disciplinaRepository = mock(DisciplinaRepository.class);
        SimuladoRepository simuladoRepository = mock(SimuladoRepository.class);

        MontarDetalheRetificacaoService montarDetalheRetificacaoService = new MontarDetalheRetificacaoService(
                notaRepository,
                alunoRepository,
                disciplinaRepository,
                simuladoRepository
        );
        ListarRetificacoesUseCase useCase = new ListarRetificacoesUseCase(
                solicitacaoRepository,
                montarDetalheRetificacaoService
        );

        SolicitacaoRetificacao primeiraSolicitacao = new SolicitacaoRetificacao(
                1L,
                10L,
                "Revisar cálculo",
                null,
                StatusSolicitacaoRetificacao.PENDENTE
        );
        SolicitacaoRetificacao segundaSolicitacao = new SolicitacaoRetificacao(
                2L,
                20L,
                "Conferir lançamento",
                null,
                StatusSolicitacaoRetificacao.EM_ANALISE
        );
        when(solicitacaoRepository.buscarTodas()).thenReturn(List.of(primeiraSolicitacao, segundaSolicitacao));

        Nota primeiraNota = new Nota(10L, 100L, 300L, 200L, 7.5);
        Nota segundaNota = new Nota(20L, 101L, 301L, 201L, 8.0);
        when(notaRepository.buscarPorIds(List.of(10L, 20L))).thenReturn(List.of(primeiraNota, segundaNota));
        when(alunoRepository.buscarPorIds(List.of(100L, 101L))).thenReturn(List.of(
                new Aluno(100L, "Ana Lima", "ana.lima@email.com", null, null),
                new Aluno(101L, "Bruno Alves", "bruno.alves@email.com", null, null)
        ));
        when(disciplinaRepository.buscarPorIds(List.of(200L, 201L))).thenReturn(List.of(
                new Disciplina(200L, "Matemática"),
                new Disciplina(201L, "Português")
        ));
        when(simuladoRepository.buscarPorIds(List.of(300L, 301L))).thenReturn(List.of(
                new Simulado(300L, "Simulado 1"),
                new Simulado(301L, "Simulado 2")
        ));

        List<SolicitacaoRetificacaoDetalheResultado> resultado = useCase.executar();

        assertEquals(2, resultado.size());
        assertEquals("Ana Lima", resultado.get(0).alunoNome());
        assertEquals("Matemática", resultado.get(0).disciplinaNome());
        assertEquals("Simulado 1", resultado.get(0).simuladoDescricao());
        assertEquals(7.5, resultado.get(0).notaAtual());
        assertEquals("Bruno Alves", resultado.get(1).alunoNome());
        assertEquals("Português", resultado.get(1).disciplinaNome());
        assertEquals("Simulado 2", resultado.get(1).simuladoDescricao());
        assertEquals(8.0, resultado.get(1).notaAtual());

        verify(notaRepository).buscarPorIds(List.of(10L, 20L));
        verify(alunoRepository).buscarPorIds(List.of(100L, 101L));
        verify(disciplinaRepository).buscarPorIds(List.of(200L, 201L));
        verify(simuladoRepository).buscarPorIds(List.of(300L, 301L));
        verify(notaRepository, never()).buscarPorId(anyLong());
        verify(alunoRepository, never()).buscarPorId(anyLong());
        verify(disciplinaRepository, never()).buscarPorId(anyLong());
        verify(simuladoRepository, never()).buscarPorId(anyLong());
    }
}

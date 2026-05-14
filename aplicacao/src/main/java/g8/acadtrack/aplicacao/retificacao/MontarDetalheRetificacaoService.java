package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MontarDetalheRetificacaoService {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final SimuladoRepository simuladoRepository;

    public MontarDetalheRetificacaoService(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository,
            SimuladoRepository simuladoRepository
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.simuladoRepository = simuladoRepository;
    }

    public SolicitacaoRetificacaoDetalheResultado montar(SolicitacaoRetificacao solicitacao) {
        Nota nota = notaRepository.buscarPorId(solicitacao.getNotaId()).orElse(null);
        Aluno aluno = nota == null ? null : alunoRepository.buscarPorId(nota.getAlunoId()).orElse(null);
        Disciplina disciplina = nota == null ? null : disciplinaRepository.buscarPorId(nota.getDisciplinaId()).orElse(null);
        Simulado simulado = nota == null ? null : simuladoRepository.buscarPorId(nota.getSimuladoId()).orElse(null);

        return montar(solicitacao, nota, aluno, disciplina, simulado);
    }

    public List<SolicitacaoRetificacaoDetalheResultado> montarEmLote(List<SolicitacaoRetificacao> solicitacoes) {
        if (solicitacoes.isEmpty()) {
            return List.of();
        }

        List<Long> notaIds = solicitacoes.stream()
                .map(SolicitacaoRetificacao::getNotaId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, Nota> notasPorId = notaRepository.buscarPorIds(notaIds)
                .stream()
                .collect(Collectors.toMap(
                        Nota::getId,
                        Function.identity(),
                        (notaAtual, notaDuplicada) -> notaAtual
                ));

        List<Nota> notas = notaIds.stream()
                .map(notasPorId::get)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, Aluno> alunosPorId = alunoRepository.buscarPorIds(idsDistintos(notas, Nota::getAlunoId))
                .stream()
                .collect(Collectors.toMap(
                        Aluno::getId,
                        Function.identity(),
                        (alunoAtual, alunoDuplicado) -> alunoAtual
                ));
        Map<Long, Disciplina> disciplinasPorId = disciplinaRepository.buscarPorIds(idsDistintos(notas, Nota::getDisciplinaId))
                .stream()
                .collect(Collectors.toMap(
                        Disciplina::getId,
                        Function.identity(),
                        (disciplinaAtual, disciplinaDuplicada) -> disciplinaAtual
                ));
        Map<Long, Simulado> simuladosPorId = simuladoRepository.buscarPorIds(idsDistintos(notas, Nota::getSimuladoId))
                .stream()
                .collect(Collectors.toMap(
                        Simulado::getId,
                        Function.identity(),
                        (simuladoAtual, simuladoDuplicado) -> simuladoAtual
                ));

        return solicitacoes.stream()
                .map(solicitacao -> {
                    Nota nota = notasPorId.get(solicitacao.getNotaId());
                    Aluno aluno = nota == null ? null : alunosPorId.get(nota.getAlunoId());
                    Disciplina disciplina = nota == null ? null : disciplinasPorId.get(nota.getDisciplinaId());
                    Simulado simulado = nota == null ? null : simuladosPorId.get(nota.getSimuladoId());
                    return montar(solicitacao, nota, aluno, disciplina, simulado);
                })
                .toList();
    }

    private SolicitacaoRetificacaoDetalheResultado montar(
            SolicitacaoRetificacao solicitacao,
            Nota nota,
            Aluno aluno,
            Disciplina disciplina,
            Simulado simulado
    ) {
        return new SolicitacaoRetificacaoDetalheResultado(
                solicitacao.getId(),
                solicitacao.getNotaId(),
                nota == null ? null : nota.getAlunoId(),
                aluno == null ? "-" : aluno.getNome(),
                nota == null ? null : nota.getDisciplinaId(),
                disciplina == null ? "-" : disciplina.getNome(),
                nota == null ? null : nota.getSimuladoId(),
                simulado == null ? "-" : simulado.getDescricao(),
                nota == null ? null : nota.getValor(),
                solicitacao.getJustificativa(),
                solicitacao.getJustificativaDecisao(),
                solicitacao.getStatus()
        );
    }

    private List<Long> idsDistintos(List<Nota> notas, Function<Nota, Long> extrairId) {
        return notas.stream()
                .map(extrairId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}

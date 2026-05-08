package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DetalharSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final AnalisarConsistenciaSimuladoService analisarConsistenciaSimuladoService;

    public DetalharSimuladoUseCase(
            SimuladoRepository simuladoRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            DisciplinaRepository disciplinaRepository,
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            AnalisarConsistenciaSimuladoService analisarConsistenciaSimuladoService
    ) {
        this.simuladoRepository = simuladoRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.analisarConsistenciaSimuladoService = analisarConsistenciaSimuladoService;
    }

public SimuladoDetalheResultado executar(Long simuladoId) {
    Simulado simulado = simuladoRepository.buscarPorId(simuladoId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Simulado nao encontrado"));

    List<SimuladoDisciplina> vinculos = simuladoDisciplinaRepository.buscarPorSimulado(simulado.getId())
            .stream()
            .filter(v -> v.getSimuladoId().equals(simulado.getId()))
            .toList();

    List<Long> disciplinasIds = vinculos.stream()
            .map(SimuladoDisciplina::getDisciplinaId)
            .distinct()
            .toList();

    List<Disciplina> disciplinas = disciplinaRepository.buscarPorIds(disciplinasIds);

    Map<Long, Disciplina> disciplinasPorId = disciplinas.stream()
            .collect(Collectors.toMap(Disciplina::getId, Function.identity()));

    ConsistenciaSimuladoResultado consistencia =
            analisarConsistenciaSimuladoService.analisar(vinculos, disciplinas);

    List<SimuladoDetalheResultado.DisciplinaVinculada> disciplinasVinculadas =
            vinculos.stream()
                    .map(v -> montarDisciplinaVinculada(v, disciplinasPorId))
                    .toList();

    List<Nota> notas = notaRepository.buscarPorSimuladoId(simulado.getId());

    Map<Long, List<Nota>> notasPorAluno = notas.stream()
            .collect(Collectors.groupingBy(Nota::getAlunoId));

    List<SimuladoDetalheResultado.AlunoParticipante> alunosParticipantes =
            notasPorAluno.entrySet()
                    .stream()
                    .map(entry -> montarAlunoParticipante(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(SimuladoDetalheResultado.AlunoParticipante::nome))
                    .toList();

    return new SimuladoDetalheResultado(
            simulado.getId(),
            simulado.getDescricao(),
            consistencia.consistente(),
            consistencia.statusConsistencia(),
            consistencia.motivoInconsistencia(),
            disciplinasVinculadas,
            new SimuladoDetalheResultado.NotasRelacionadas(notas.size(), notasPorAluno.size()),
            alunosParticipantes
    );
}

    private SimuladoDetalheResultado.DisciplinaVinculada montarDisciplinaVinculada(
            SimuladoDisciplina vinculo,
            Map<Long, Disciplina> disciplinasPorId
    ) {
        Disciplina disciplina = disciplinasPorId.get(vinculo.getDisciplinaId());
        if (disciplina == null) {
            return new SimuladoDetalheResultado.DisciplinaVinculada(
                    vinculo.getDisciplinaId(),
                    "Disciplina " + vinculo.getDisciplinaId(),
                    "INEXISTENTE"
            );
        }

        return new SimuladoDetalheResultado.DisciplinaVinculada(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getStatus().name()
        );
    }

    private SimuladoDetalheResultado.AlunoParticipante montarAlunoParticipante(Long alunoId, List<Nota> notas) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId).orElse(null);
        double media = notas.stream()
                .mapToDouble(Nota::getValor)
                .average()
                .orElse(0.0);

        return new SimuladoDetalheResultado.AlunoParticipante(
                alunoId,
                aluno == null ? "Aluno " + alunoId : aluno.getNome(),
                notas.size(),
                media
        );
    }
}

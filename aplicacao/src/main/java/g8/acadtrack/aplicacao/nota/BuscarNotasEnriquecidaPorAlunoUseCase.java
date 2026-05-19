package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.disciplina.ListarDisciplinasUseCase;
import g8.acadtrack.aplicacao.simulado.ListarSimuladosUseCase;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BuscarNotasEnriquecidaPorAlunoUseCase {

    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;
    private final ListarDisciplinasUseCase listarDisciplinasUseCase;
    private final ListarSimuladosUseCase listarSimuladosUseCase;

    public BuscarNotasEnriquecidaPorAlunoUseCase(
            BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase,
            ListarDisciplinasUseCase listarDisciplinasUseCase,
            ListarSimuladosUseCase listarSimuladosUseCase) {
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
        this.listarDisciplinasUseCase = listarDisciplinasUseCase;
        this.listarSimuladosUseCase = listarSimuladosUseCase;
    }

    public List<NotaEnriquecida> executar(Long alunoId) {
        List<Nota> notas = buscarNotasPorAlunoUseCase.executar(alunoId);

        List<Long> disciplinaIds = notas.stream().map(Nota::getDisciplinaId).distinct().toList();
        List<Long> simuladoIds = notas.stream().map(Nota::getSimuladoId).distinct().toList();

        Map<Long, String> nomePorDisciplinaId = listarDisciplinasUseCase.executarPorIds(disciplinaIds)
                .stream()
                .collect(Collectors.toMap(Disciplina::getId, Disciplina::getNome));

        Map<Long, String> descricaoPorSimuladoId = listarSimuladosUseCase.executarPorIds(simuladoIds)
                .stream()
                .collect(Collectors.toMap(Simulado::getId, Simulado::getDescricao));

        return notas.stream()
                .map(nota -> new NotaEnriquecida(
                        nota,
                        nomePorDisciplinaId.get(nota.getDisciplinaId()),
                        descricaoPorSimuladoId.get(nota.getSimuladoId())
                ))
                .toList();
    }
}

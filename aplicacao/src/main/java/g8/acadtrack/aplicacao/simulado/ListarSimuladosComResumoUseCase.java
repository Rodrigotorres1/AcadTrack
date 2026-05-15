package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarSimuladosComResumoUseCase {

    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AnalisarConsistenciaSimuladoService analisarConsistenciaSimuladoService;

    public ListarSimuladosComResumoUseCase(
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            AnalisarConsistenciaSimuladoService analisarConsistenciaSimuladoService
    ) {
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.analisarConsistenciaSimuladoService = analisarConsistenciaSimuladoService;
    }

    public List<SimuladoResumoResultado> executar() {
        return simuladoRepository.buscarTodos()
                .stream()
                .map(this::montarResumo)
                .toList();
    }

    private SimuladoResumoResultado montarResumo(Simulado simulado) {
        List<SimuladoDisciplina> vinculos = simulado.listarDisciplinas();
        List<Long> disciplinasIds = vinculos.stream()
                .map(SimuladoDisciplina::getDisciplinaId)
                .distinct()
                .toList();
        List<Disciplina> disciplinas = disciplinaRepository.buscarPorIds(disciplinasIds);
        ConsistenciaSimuladoResultado consistencia = analisarConsistenciaSimuladoService.analisar(vinculos, disciplinas);

        return new SimuladoResumoResultado(
                simulado.getId(),
                simulado.getDescricao(),
                vinculos.size(),
                consistencia.consistente(),
                consistencia.statusConsistencia(),
                consistencia.motivoInconsistencia()
        );
    }
}

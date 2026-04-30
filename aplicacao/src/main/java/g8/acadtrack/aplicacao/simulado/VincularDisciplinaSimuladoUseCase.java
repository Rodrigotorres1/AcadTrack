package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class VincularDisciplinaSimuladoUseCase {

    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public VincularDisciplinaSimuladoUseCase(
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public SimuladoDisciplina executar(Long simuladoId, Long disciplinaId, double peso) {
        simuladoRepository.buscarPorId(simuladoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Simulado não encontrado"));

        Disciplina disciplina = disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina não encontrada"));

        if (!disciplina.estaAtiva()) {
            throw new IllegalStateException("Disciplina inativa não pode ser vinculada a simulado");
        }

        boolean jaVinculada = simuladoDisciplinaRepository.buscarPorSimulado(simuladoId)
                .stream()
                .anyMatch(sd -> sd.getDisciplinaId().equals(disciplinaId));

        if (jaVinculada) {
            throw new IllegalStateException("Disciplina já vinculada a este simulado");
        }

        return simuladoDisciplinaRepository.salvar(
                new SimuladoDisciplina(null, simuladoId, disciplinaId, peso)
        );
    }
}

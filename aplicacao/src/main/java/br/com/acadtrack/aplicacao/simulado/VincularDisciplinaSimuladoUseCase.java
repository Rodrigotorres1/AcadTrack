package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class VincularDisciplinaSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;

    public VincularDisciplinaSimuladoUseCase(
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository
    ) {
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
    }

    public SimuladoDisciplina executar(Long simuladoId, Long disciplinaId, double peso) {
        Simulado simulado = simuladoRepository.buscarPorId(simuladoId)
                .orElseThrow(() -> new IllegalArgumentException("Simulado não encontrado"));

        Disciplina disciplina = disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada"));

        SimuladoDisciplina vinculo = new SimuladoDisciplina(
                null,
                simulado.getId(),
                disciplina.getId(),
                peso
        );

        return simuladoDisciplinaRepository.salvar(vinculo);
    }
}
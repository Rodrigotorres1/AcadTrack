package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import org.springframework.stereotype.Service;

@Service
public class VincularDisciplinaSimuladoUseCase {

    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;

    public VincularDisciplinaSimuladoUseCase(SimuladoDisciplinaRepository simuladoDisciplinaRepository) {
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
    }

    public SimuladoDisciplina executar(Long simuladoId, Long disciplinaId, double peso) {
        SimuladoDisciplina simuladoDisciplina = new SimuladoDisciplina(
                null,
                simuladoId,
                disciplinaId,
                peso
        );

        return simuladoDisciplinaRepository.salvar(simuladoDisciplina);
    }
}
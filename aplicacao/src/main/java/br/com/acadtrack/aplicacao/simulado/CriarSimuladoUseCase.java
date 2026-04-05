package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioacademico.simulado.Simulado;
import br.com.acadtrack.dominioacademico.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;

    public CriarSimuladoUseCase(SimuladoRepository simuladoRepository) {
        this.simuladoRepository = simuladoRepository;
    }

    public void executar(Long id, String descricao) {
        Simulado simulado = new Simulado(id, descricao);
        simuladoRepository.salvar(simulado);
    }
}
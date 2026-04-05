package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;

    public CriarSimuladoUseCase(SimuladoRepository simuladoRepository) {
        this.simuladoRepository = simuladoRepository;
    }

    public Simulado executar(String descricao) {
        Simulado simulado = new Simulado(null, descricao);
        return simuladoRepository.salvar(simulado);
    }
}
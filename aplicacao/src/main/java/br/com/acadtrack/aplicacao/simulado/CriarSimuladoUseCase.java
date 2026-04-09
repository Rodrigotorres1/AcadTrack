package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriarSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;

    public CriarSimuladoUseCase(SimuladoRepository simuladoRepository) {
        this.simuladoRepository = simuladoRepository;
    }

    public Simulado executar(String descricao, List<Long> disciplinasIds) {
        if (disciplinasIds == null || disciplinasIds.isEmpty()) {
            throw new IllegalArgumentException("O simulado deve possuir pelo menos uma disciplina");
        }

        Simulado simulado = new Simulado(null, descricao);
        return simuladoRepository.salvar(simulado);
    }
}
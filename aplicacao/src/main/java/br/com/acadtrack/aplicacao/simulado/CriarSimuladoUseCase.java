package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriarSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public CriarSimuladoUseCase(
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public Simulado executar(String descricao, List<Long> disciplinasIds) {
        if (disciplinasIds == null || disciplinasIds.isEmpty()) {
            throw new IllegalArgumentException("O simulado deve possuir pelo menos uma disciplina");
        }

        List<Disciplina> disciplinas = disciplinaRepository.buscarPorIds(disciplinasIds);

        if (disciplinas.size() != disciplinasIds.size()) {
            throw new IllegalArgumentException("Uma ou mais disciplinas não existem");
        }

        Simulado simulado = new Simulado(null, descricao);

        return simuladoRepository.salvar(simulado);
    }
}
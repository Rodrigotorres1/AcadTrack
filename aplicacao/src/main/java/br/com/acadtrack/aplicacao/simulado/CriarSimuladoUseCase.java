package br.com.acadtrack.aplicacao.simulado;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.Simulado;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CriarSimuladoUseCase {

    private static final double PESO_PADRAO = 1.0;

    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final ValidarComposicaoSimuladoService validarComposicaoSimuladoService;

    public CriarSimuladoUseCase(
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            ValidarComposicaoSimuladoService validarComposicaoSimuladoService
    ) {
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.validarComposicaoSimuladoService = validarComposicaoSimuladoService;
    }

    @Transactional
    public Simulado executar(String descricao, List<Long> disciplinasIds) {
        if (simuladoRepository.buscarPorDescricaoNormalizada(descricao).isPresent()) {
            throw new IllegalStateException("Já existe simulado cadastrado com esta descrição");
        }

        validarComposicaoSimuladoService.validarDisciplinasParaCriacao(disciplinasIds);

        List<Disciplina> disciplinas = disciplinaRepository.buscarPorIds(disciplinasIds);

        if (disciplinas.size() != disciplinasIds.size()) {
            throw new EntidadeNaoEncontradaException("Uma ou mais disciplinas não existem");
        }

        boolean existeDisciplinaInativa = disciplinas.stream().anyMatch(disciplina -> !disciplina.estaAtiva());
        if (existeDisciplinaInativa) {
            throw new IllegalStateException("Disciplina inativa não pode ser vinculada a simulado");
        }

        Simulado simuladoSalvo = simuladoRepository.salvar(new Simulado(null, descricao));

        for (Disciplina disciplina : disciplinas) {
            simuladoDisciplinaRepository.salvar(new SimuladoDisciplina(
                    null,
                    simuladoSalvo.getId(),
                    disciplina.getId(),
                    PESO_PADRAO
            ));
        }

        return simuladoSalvo;
    }
}

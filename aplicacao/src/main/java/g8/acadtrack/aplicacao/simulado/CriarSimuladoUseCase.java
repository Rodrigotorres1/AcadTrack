package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CriarSimuladoUseCase {

    private static final double PESO_PADRAO = 1.0;

    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ValidarComposicaoSimuladoService validarComposicaoSimuladoService;

    public CriarSimuladoUseCase(
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            ValidarComposicaoSimuladoService validarComposicaoSimuladoService
    ) {
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.validarComposicaoSimuladoService = validarComposicaoSimuladoService;
    }

    @Transactional
    public Simulado executar(String descricao, List<Long> disciplinasIds) {
        if (simuladoRepository.buscarPorDescricaoNormalizada(descricao).isPresent()) {
            throw new ConflitoDeEstadoException("Já existe simulado cadastrado com esta descrição");
        }

        validarComposicaoSimuladoService.validarDisciplinasParaCriacao(disciplinasIds);

        List<Disciplina> disciplinas = disciplinaRepository.buscarPorIds(disciplinasIds);

        if (disciplinas.size() != disciplinasIds.size()) {
            throw new EntidadeNaoEncontradaException("Uma ou mais disciplinas não existem");
        }

        boolean existeDisciplinaInativa = disciplinas.stream().anyMatch(disciplina -> !disciplina.estaAtiva());
        if (existeDisciplinaInativa) {
            throw new RegraDeNegocioException("Disciplina inativa não pode ser vinculada a simulado");
        }

        Simulado simuladoSalvo = simuladoRepository.salvar(new Simulado(null, descricao));

        for (Disciplina disciplina : disciplinas) {
            simuladoSalvo.adicionarDisciplina(disciplina.getId(), PESO_PADRAO);
        }

        return simuladoRepository.salvar(simuladoSalvo);
    }
}

package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtualizarSimuladoUseCase {

    private static final double PESO_PADRAO = 1.0;

    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;
    private final NotaRepository notaRepository;
    private final ValidarComposicaoSimuladoService validarComposicaoSimuladoService;

    public AtualizarSimuladoUseCase(
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            NotaRepository notaRepository,
            ValidarComposicaoSimuladoService validarComposicaoSimuladoService
    ) {
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
        this.notaRepository = notaRepository;
        this.validarComposicaoSimuladoService = validarComposicaoSimuladoService;
    }

    @Transactional
    public Simulado executar(Long simuladoId, String descricao, List<Long> disciplinasIds) {
        Simulado simulado = simuladoRepository.buscarPorId(simuladoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Simulado não encontrado"));

        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição é obrigatória");
        }

        String descricaoTrimmed = descricao.trim();
        if (!descricaoTrimmed.equalsIgnoreCase(simulado.getDescricao())) {
            simuladoRepository.buscarPorDescricaoNormalizada(descricaoTrimmed)
                    .ifPresent(outro -> {
                        throw new ConflitoDeEstadoException("Já existe simulado cadastrado com esta descrição");
                    });
        }

        validarComposicaoSimuladoService.validarDisciplinasParaCriacao(disciplinasIds);

        List<Disciplina> disciplinas = disciplinaRepository.buscarPorIds(disciplinasIds);
        if (disciplinas.size() != disciplinasIds.size()) {
            throw new EntidadeNaoEncontradaException("Uma ou mais disciplinas não existem");
        }

        boolean existeDisciplinaInativa = disciplinas.stream().anyMatch(d -> !d.estaAtiva());
        if (existeDisciplinaInativa) {
            throw new RegraDeNegocioException("Disciplina inativa não pode ser vinculada a simulado");
        }

        boolean temNotas = !notaRepository.buscarPorSimuladoId(simuladoId).isEmpty();
        if (temNotas) {
            throw new ConflitoDeEstadoException(
                    "Simulado com notas lançadas não pode ser alterado");
        }

        simulado.atualizar(descricaoTrimmed);

        simuladoDisciplinaRepository.excluirPorSimulado(simuladoId);
        for (Disciplina disciplina : disciplinas) {
            simuladoDisciplinaRepository.salvar(new SimuladoDisciplina(
                    null, simuladoId, disciplina.getId(), PESO_PADRAO
            ));
        }

        return simuladoRepository.salvar(simulado);
    }
}

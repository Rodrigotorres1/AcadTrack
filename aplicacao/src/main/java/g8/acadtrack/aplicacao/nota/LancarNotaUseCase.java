package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.nota.validacao.ValidacaoLancamentoNotaService;
import g8.acadtrack.aplicacao.riscoacademico.PublicadorRiscoAcademico;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LancarNotaUseCase {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final ValidacaoLancamentoNotaService validacaoLancamentoNotaService;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;
    private final PublicadorRiscoAcademico publicadorRiscoAcademico;
    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;

    public LancarNotaUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            ValidacaoLancamentoNotaService validacaoLancamentoNotaService,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase,
            PublicadorRiscoAcademico publicadorRiscoAcademico,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.validacaoLancamentoNotaService = validacaoLancamentoNotaService;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
        this.publicadorRiscoAcademico = publicadorRiscoAcademico;
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
    }

    @Transactional
    public Nota executar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        validacaoLancamentoNotaService.validar(alunoId, simuladoId, disciplinaId, valor);
        boolean disciplinaVinculada = simuladoDisciplinaRepository.buscarPorSimulado(simuladoId)
                .stream()
                .anyMatch(vinculo -> vinculo.getDisciplinaId().equals(disciplinaId));
        if (!disciplinaVinculada) {
            throw new RegraDeNegocioException("Disciplina nao vinculada ao simulado");
        }

        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno nao encontrado"));

        Nota nota = new Nota(null, alunoId, simuladoId, disciplinaId, valor);
        Nota notaSalva = notaRepository.salvar(nota);

        double mediaAtual = avaliacaoAcademicaService.calcularMedia(notaRepository.buscarPorAlunoId(alunoId));
        aluno.atualizarDesempenhoAcademico(mediaAtual, avaliacaoAcademicaService.calcularSituacao(mediaAtual));
        alunoRepository.salvar(aluno);

        AnaliseDesempenhoAcademicoResultado analise = analisarDesempenhoAcademicoUseCase.executar(alunoId);
        publicadorRiscoAcademico.publicarSeRiscoNotificavel(analise);

        return notaSalva;
    }
}

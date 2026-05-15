package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.aplicacao.evento.DomainEventPublisher;
import g8.acadtrack.aplicacao.riscoacademico.AnaliseRiscoAcademicoResultado;
import g8.acadtrack.aplicacao.riscoacademico.AnalisarRiscoAcademicoAlunoService;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AprovarRetificacaoUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final AnalisarRiscoAcademicoAlunoService analisarRiscoAcademicoAlunoService;
    private final DomainEventPublisher domainEventPublisher;

    public AprovarRetificacaoUseCase(
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository,
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            AnalisarRiscoAcademicoAlunoService analisarRiscoAcademicoAlunoService,
            DomainEventPublisher domainEventPublisher
    ) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.analisarRiscoAcademicoAlunoService = analisarRiscoAcademicoAlunoService;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public SolicitacaoRetificacao executar(Long solicitacaoId, double novoValorNota, String justificativaDecisao) {
        SolicitacaoRetificacao solicitacao = solicitacaoRetificacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitação de retificação não encontrada"));

        Nota nota = notaRepository.buscarPorId(solicitacao.getNotaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nota não encontrada"));

        solicitacao.aprovar(justificativaDecisao);
        solicitacao = solicitacaoRetificacaoRepository.salvar(solicitacao);
        nota.atualizarValor(novoValorNota);
        notaRepository.salvar(nota);

        Aluno aluno = alunoRepository.buscarPorId(nota.getAlunoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        List<Nota> notasDoAluno = notaRepository.buscarPorAlunoId(aluno.getId());
        AnaliseRiscoAcademicoResultado analise = analisarRiscoAcademicoAlunoService.analisar(aluno.getId(), notasDoAluno);
        aluno.atualizarDesempenhoAcademico(analise.mediaGeral(), analise.situacaoAcademica());
        aluno.registrarRiscoAcademicoIdentificado(analise.nivelRisco());
        alunoRepository.salvar(aluno);

        domainEventPublisher.publicar(aluno.liberarEventosDominio());

        return solicitacao;
    }
}

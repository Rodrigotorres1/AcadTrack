package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.aplicacao.nota.AvaliacaoAcademicaService;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AprovarRetificacaoUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;

    public AprovarRetificacaoUseCase(
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository,
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService
    ) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
    }

    @Transactional
    public SolicitacaoRetificacao executar(Long solicitacaoId, double novoValorNota, String justificativaDecisao) {
        SolicitacaoRetificacao solicitacao = solicitacaoRetificacaoRepository.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitação de retificação não encontrada"));

        Nota nota = notaRepository.buscarPorId(solicitacao.getNotaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Nota não encontrada"));

        solicitacao.aprovar(justificativaDecisao);
        nota.atualizarValor(novoValorNota);
        notaRepository.salvar(nota);

        Aluno aluno = alunoRepository.buscarPorId(nota.getAlunoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        double mediaAtual = avaliacaoAcademicaService.calcularMedia(notaRepository.buscarPorAlunoId(aluno.getId()));
        aluno.atualizarDesempenhoAcademico(mediaAtual, avaliacaoAcademicaService.calcularSituacao(mediaAtual));
        alunoRepository.salvar(aluno);

        return solicitacaoRetificacaoRepository.salvar(solicitacao);
    }
}

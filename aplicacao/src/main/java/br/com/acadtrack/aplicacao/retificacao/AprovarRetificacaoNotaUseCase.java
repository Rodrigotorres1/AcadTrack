package br.com.acadtrack.aplicacao.retificacao;

import br.com.acadtrack.aplicacao.nota.AvaliacaoAcademicaService;
import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import br.com.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AprovarRetificacaoNotaUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;
    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;

    public AprovarRetificacaoNotaUseCase(
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

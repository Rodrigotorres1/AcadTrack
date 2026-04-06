package br.com.acadtrack.dominioavaliacao.retificacao;

import java.util.Optional;
import java.util.List;


public interface SolicitacaoRetificacaoRepository {

    SolicitacaoRetificacao salvar(SolicitacaoRetificacao solicitacao);

    Optional<SolicitacaoRetificacao> buscarPorId(Long id);

    List<SolicitacaoRetificacao> buscarTodas();
}
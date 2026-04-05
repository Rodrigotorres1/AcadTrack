package br.com.acadtrack.dominioavaliacao.retificacao;

import java.util.Optional;

public interface SolicitacaoRetificacaoRepository {

    void salvar(SolicitacaoRetificacao solicitacao);

    Optional<SolicitacaoRetificacao> buscarPorId(Long id);
}
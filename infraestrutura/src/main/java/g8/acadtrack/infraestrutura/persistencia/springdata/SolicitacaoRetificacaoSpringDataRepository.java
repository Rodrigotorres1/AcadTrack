package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.dominioavaliacao.retificacao.StatusSolicitacaoRetificacao;
import g8.acadtrack.infraestrutura.persistencia.entidade.SolicitacaoRetificacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRetificacaoSpringDataRepository extends JpaRepository<SolicitacaoRetificacaoJpaEntity, Long> {
    boolean existsByNotaIdAndStatusIn(Long notaId, List<StatusSolicitacaoRetificacao> status);

    List<SolicitacaoRetificacaoJpaEntity> findByNotaId(Long notaId);
}

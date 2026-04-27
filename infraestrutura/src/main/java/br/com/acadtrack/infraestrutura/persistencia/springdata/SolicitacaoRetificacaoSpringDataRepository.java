package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.SolicitacaoRetificacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRetificacaoSpringDataRepository extends JpaRepository<SolicitacaoRetificacaoJpaEntity, Long> {
    boolean existsByNotaIdAndStatusIn(Long notaId, List<String> status);

    List<SolicitacaoRetificacaoJpaEntity> findByNotaId(Long notaId);
}
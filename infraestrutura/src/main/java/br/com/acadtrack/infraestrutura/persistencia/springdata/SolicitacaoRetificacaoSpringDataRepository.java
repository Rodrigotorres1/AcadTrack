package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.SolicitacaoRetificacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRetificacaoSpringDataRepository extends JpaRepository<SolicitacaoRetificacaoJpaEntity, Long> {
}
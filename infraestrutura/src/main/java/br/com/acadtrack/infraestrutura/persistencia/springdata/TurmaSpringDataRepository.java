package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.TurmaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaSpringDataRepository extends JpaRepository<TurmaJpaEntity, Long> {
}
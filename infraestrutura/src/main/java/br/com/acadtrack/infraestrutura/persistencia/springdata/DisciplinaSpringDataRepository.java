package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.DisciplinaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaSpringDataRepository extends JpaRepository<DisciplinaJpaEntity, Long> {
}
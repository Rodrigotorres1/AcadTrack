package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.ProfessorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorSpringDataRepository extends JpaRepository<ProfessorJpaEntity, Long> {
}
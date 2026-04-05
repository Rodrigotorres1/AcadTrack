package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoSpringDataRepository extends JpaRepository<AlunoJpaEntity, Long> {
}
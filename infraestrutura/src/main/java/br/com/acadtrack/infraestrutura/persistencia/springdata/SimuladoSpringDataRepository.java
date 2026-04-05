package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.SimuladoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimuladoSpringDataRepository extends JpaRepository<SimuladoJpaEntity, Long> {
}
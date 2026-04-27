package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.SimuladoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SimuladoSpringDataRepository extends JpaRepository<SimuladoJpaEntity, Long> {
    Optional<SimuladoJpaEntity> findFirstByDescricaoIgnoreCase(String descricao);
}
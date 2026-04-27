package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.DisciplinaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisciplinaSpringDataRepository extends JpaRepository<DisciplinaJpaEntity, Long> {
    Optional<DisciplinaJpaEntity> findFirstByNomeIgnoreCase(String nome);
}
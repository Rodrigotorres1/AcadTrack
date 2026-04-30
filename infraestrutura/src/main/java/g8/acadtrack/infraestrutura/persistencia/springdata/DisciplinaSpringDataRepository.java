package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.DisciplinaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisciplinaSpringDataRepository extends JpaRepository<DisciplinaJpaEntity, Long> {
    Optional<DisciplinaJpaEntity> findFirstByNomeIgnoreCase(String nome);
}
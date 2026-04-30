package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.TurmaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaSpringDataRepository extends JpaRepository<TurmaJpaEntity, Long> {
}
package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.ProfessorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorSpringDataRepository extends JpaRepository<ProfessorJpaEntity, Long> {
}
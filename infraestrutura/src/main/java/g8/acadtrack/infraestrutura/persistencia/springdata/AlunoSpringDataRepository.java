package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoSpringDataRepository extends JpaRepository<AlunoJpaEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);
}
package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoSpringDataRepository extends JpaRepository<AlunoJpaEntity, Long> {

    List<AlunoJpaEntity> findByResponsavelId(Long responsavelId);

    boolean existsByEmailIgnoreCase(String email);
}

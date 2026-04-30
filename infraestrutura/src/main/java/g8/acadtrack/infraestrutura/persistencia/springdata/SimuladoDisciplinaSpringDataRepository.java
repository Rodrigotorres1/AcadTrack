package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.SimuladoDisciplinaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimuladoDisciplinaSpringDataRepository extends JpaRepository<SimuladoDisciplinaJpaEntity, Long> {

    List<SimuladoDisciplinaJpaEntity> findBySimuladoId(Long simuladoId);

    boolean existsByDisciplinaId(Long disciplinaId);
}
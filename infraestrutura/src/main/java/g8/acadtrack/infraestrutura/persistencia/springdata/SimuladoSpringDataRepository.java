package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.SimuladoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SimuladoSpringDataRepository extends JpaRepository<SimuladoJpaEntity, Long> {
    Optional<SimuladoJpaEntity> findFirstByDescricaoIgnoreCase(String descricao);

    List<SimuladoJpaEntity> findByIdIn(List<Long> ids);
}

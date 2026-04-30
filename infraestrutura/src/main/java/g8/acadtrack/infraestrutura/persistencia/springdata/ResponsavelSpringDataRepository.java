package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.ResponsavelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResponsavelSpringDataRepository extends JpaRepository<ResponsavelJpaEntity, Long> {
}
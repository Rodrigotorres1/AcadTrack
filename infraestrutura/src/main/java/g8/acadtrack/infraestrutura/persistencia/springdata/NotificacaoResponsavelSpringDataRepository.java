package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.NotificacaoResponsavelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoResponsavelSpringDataRepository extends JpaRepository<NotificacaoResponsavelJpaEntity, Long> {

    List<NotificacaoResponsavelJpaEntity> findByResponsavelIdOrderByDataCriacaoDesc(Long responsavelId);

    boolean existsByAlunoIdAndResponsavelIdAndNivelRiscoAndStatus(
            Long alunoId, Long responsavelId, String nivelRisco, String status);
}

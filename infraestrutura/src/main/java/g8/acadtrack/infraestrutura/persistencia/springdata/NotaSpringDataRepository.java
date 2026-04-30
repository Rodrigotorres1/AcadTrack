package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaSpringDataRepository extends JpaRepository<NotaJpaEntity, Long> {

    List<NotaJpaEntity> findByAlunoIdAndSimuladoId(Long alunoId, Long simuladoId);

    List<NotaJpaEntity> findByAlunoId(Long alunoId);

    List<NotaJpaEntity> findBySimuladoId(Long simuladoId);

    boolean existsByDisciplinaId(Long disciplinaId);

    boolean existsByAlunoIdAndSimuladoIdAndDisciplinaId(Long alunoId, Long simuladoId, Long disciplinaId);
}
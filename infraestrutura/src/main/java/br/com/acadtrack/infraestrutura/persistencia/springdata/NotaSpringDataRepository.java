package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaSpringDataRepository extends JpaRepository<NotaJpaEntity, Long> {

    List<NotaJpaEntity> findByAlunoIdAndSimuladoId(Long alunoId, Long simuladoId);

    List<NotaJpaEntity> findByAlunoId(Long alunoId);

    List<NotaJpaEntity> findBySimuladoId(Long simuladoId);
}
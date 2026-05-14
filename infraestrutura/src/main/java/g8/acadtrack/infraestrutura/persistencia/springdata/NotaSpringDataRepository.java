package g8.acadtrack.infraestrutura.persistencia.springdata;

import g8.acadtrack.infraestrutura.persistencia.entidade.AlunoJpaEntity;
import g8.acadtrack.infraestrutura.persistencia.entidade.NotaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaSpringDataRepository extends JpaRepository<NotaJpaEntity, Long> {

    List<NotaJpaEntity> findByAlunoIdAndSimuladoId(Long alunoId, Long simuladoId);

    List<NotaJpaEntity> findByAlunoId(Long alunoId);

    List<NotaJpaEntity> findByAlunoIdIn(List<Long> alunoIds);

    List<NotaJpaEntity> findBySimuladoId(Long simuladoId);

    @Query("select distinct aluno from NotaJpaEntity nota join nota.aluno aluno")
    List<AlunoJpaEntity> findAlunosComNotas();

    @Query("select count(distinct aluno.id) from NotaJpaEntity nota join nota.aluno aluno")
    long countAlunosComNotas();

    @Query("select count(distinct aluno.id) from NotaJpaEntity nota join nota.aluno aluno where aluno.mediaAritmetica > :mediaAritmetica")
    long countAlunosComNotasComMediaMaiorQue(@Param("mediaAritmetica") double mediaAritmetica);

    boolean existsByDisciplinaId(Long disciplinaId);

    boolean existsByAlunoIdAndSimuladoIdAndDisciplinaId(Long alunoId, Long simuladoId, Long disciplinaId);
}

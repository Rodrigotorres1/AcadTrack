package br.com.acadtrack.infraestrutura.persistencia.springdata;

import br.com.acadtrack.infraestrutura.persistencia.entidade.ResponsavelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponsavelSpringDataRepository extends JpaRepository<ResponsavelJpaEntity, Long> {

    Optional<ResponsavelJpaEntity> findByAlunoId(Long alunoId);
}
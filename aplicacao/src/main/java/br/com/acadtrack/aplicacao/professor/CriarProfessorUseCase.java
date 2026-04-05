package br.com.acadtrack.aplicacao.professor;

import br.com.acadtrack.dominiousuarios.professor.Professor;
import br.com.acadtrack.dominiousuarios.professor.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarProfessorUseCase {

    private final ProfessorRepository professorRepository;

    public CriarProfessorUseCase(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void executar(Long id, String nome, String email) {
        Professor professor = new Professor(id, nome, email);
        professorRepository.salvar(professor);
    }
}
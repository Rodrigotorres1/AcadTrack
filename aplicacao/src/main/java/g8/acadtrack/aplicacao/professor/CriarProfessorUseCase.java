package g8.acadtrack.aplicacao.professor;

import g8.acadtrack.dominiousuarios.professor.Professor;
import g8.acadtrack.dominiousuarios.professor.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarProfessorUseCase {

    private final ProfessorRepository professorRepository;

    public CriarProfessorUseCase(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Professor executar(String nome, String email) {
        Professor professor = new Professor(null, nome, email);
        return professorRepository.salvar(professor);
    }
}
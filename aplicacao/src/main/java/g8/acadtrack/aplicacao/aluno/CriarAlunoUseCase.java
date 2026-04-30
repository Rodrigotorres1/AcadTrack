package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public CriarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(String nome, String email) {
        if (alunoRepository.existeAlunoComEmailIgnorandoMaiusculas(email)) {
            throw new IllegalStateException("Já existe aluno cadastrado com este e-mail.");
        }
        Aluno aluno = new Aluno(null, nome, email, null, null);
        return alunoRepository.salvar(aluno);
    }
}
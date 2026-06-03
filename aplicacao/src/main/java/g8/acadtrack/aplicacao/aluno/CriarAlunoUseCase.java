package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.email.Email;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public CriarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno executar(String nome, String email) {
        Email emailVO = new Email(email);
        if (alunoRepository.existeAlunoComEmailIgnorandoMaiusculas(emailVO.getEndereco())) {
            throw new ConflitoDeEstadoException("Já existe aluno cadastrado com este e-mail");
        }
        Aluno aluno = new Aluno(null, nome, emailVO.getEndereco(), null, null);
        return alunoRepository.salvar(aluno);
    }
}

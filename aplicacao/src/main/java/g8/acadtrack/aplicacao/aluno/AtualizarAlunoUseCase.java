package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.email.Email;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public AtualizarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno executar(Long alunoId, String nome, String email) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        Email emailVO = new Email(email);
        if (!emailVO.getEndereco().equalsIgnoreCase(aluno.getEmail())) {
            if (alunoRepository.existeAlunoComEmailIgnorandoMaiusculas(emailVO.getEndereco())) {
                throw new ConflitoDeEstadoException("Já existe outro aluno com este e-mail");
            }
        }

        aluno.atualizar(nome, emailVO.getEndereco());
        return alunoRepository.salvar(aluno);
    }
}

package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class AtualizarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public AtualizarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(Long alunoId, String nome, String email, Long turmaId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        String emailNovo = email != null ? email.trim() : null;
        String emailAtual = aluno.getEmail() != null ? aluno.getEmail().trim() : null;

        if (emailNovo != null && !emailNovo.equalsIgnoreCase(emailAtual)) {
            if (alunoRepository.existeAlunoComEmailIgnorandoMaiusculas(emailNovo)) {
                throw new ConflitoDeEstadoException("Já existe outro aluno com este e-mail");
            }
        }

        aluno.atualizar(nome, email);
        if (turmaId != null) {
            aluno.substituirTurma(turmaId);
        }
        return alunoRepository.salvar(aluno);
    }
}

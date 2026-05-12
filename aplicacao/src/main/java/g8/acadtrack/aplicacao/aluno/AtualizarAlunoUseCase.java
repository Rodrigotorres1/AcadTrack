package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import g8.acadtrack.dominiocompartilhado.email.Email;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarAlunoUseCase {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public AtualizarAlunoUseCase(AlunoRepository alunoRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public Aluno executar(Long alunoId, String nome, String email, Long turmaId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        String emailNovo = Email.normalizar(email);
        String emailAtual = aluno.getEmail();

        if (!emailNovo.equalsIgnoreCase(emailAtual)) {
            if (alunoRepository.existeAlunoComEmailIgnorandoMaiusculas(emailNovo)) {
                throw new ConflitoDeEstadoException("Já existe outro aluno com este e-mail");
            }
        }

        aluno.atualizar(nome, emailNovo);
        if (turmaId != null) {
            turmaRepository.buscarPorId(turmaId)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Turma não encontrada"));
            aluno.substituirTurma(turmaId);
        }
        return alunoRepository.salvar(aluno);
    }
}

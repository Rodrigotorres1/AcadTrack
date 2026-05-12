package g8.acadtrack.aplicacao.aluno;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InativarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public InativarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public Aluno executar(Long alunoId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado."));
        aluno.inativar();
        return alunoRepository.salvar(aluno);
    }
}

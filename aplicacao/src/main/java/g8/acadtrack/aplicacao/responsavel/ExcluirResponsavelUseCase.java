package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirResponsavelUseCase {

    private final ResponsavelRepository responsavelRepository;
    private final AlunoRepository alunoRepository;

    public ExcluirResponsavelUseCase(
            ResponsavelRepository responsavelRepository,
            AlunoRepository alunoRepository
    ) {
        this.responsavelRepository = responsavelRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void executar(Long responsavelId) {
        responsavelRepository.buscarPorId(responsavelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsável não encontrado"));

        alunoRepository.buscarPorResponsavelId(responsavelId)
                .forEach(this::removerResponsavelDoAluno);

        responsavelRepository.excluirPorId(responsavelId);
    }

    private void removerResponsavelDoAluno(Aluno aluno) {
        aluno.removerResponsavel();
        alunoRepository.salvar(aluno);
    }
}

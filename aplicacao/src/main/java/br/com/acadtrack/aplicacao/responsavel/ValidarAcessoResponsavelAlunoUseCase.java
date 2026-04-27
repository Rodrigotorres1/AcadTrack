package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class ValidarAcessoResponsavelAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public ValidarAcessoResponsavelAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(Long alunoId, Long responsavelId, PermissaoResponsavel permissaoResponsavel) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));
        aluno.validarAcessoResponsavel(responsavelId, permissaoResponsavel);
        return aluno;
    }
}

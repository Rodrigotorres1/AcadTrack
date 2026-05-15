package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AcessoResponsavelAlunoProxy implements AcessoResponsavelAlunoService {

    private final AlunoServiceReal alunoServiceReal;
    private final ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase;

    public AcessoResponsavelAlunoProxy(
            AlunoServiceReal alunoServiceReal,
            ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase
    ) {
        this.alunoServiceReal = alunoServiceReal;
        this.validarAcessoResponsavelAlunoUseCase = validarAcessoResponsavelAlunoUseCase;
    }

    @Override
    public Aluno executar(Long alunoId, Long responsavelId, PermissaoResponsavel permissaoResponsavel) {
        Aluno aluno = alunoServiceReal.executar(alunoId, responsavelId, permissaoResponsavel);
        validarAcessoResponsavelAlunoUseCase.executar(aluno, responsavelId, permissaoResponsavel);
        return aluno;
    }
}

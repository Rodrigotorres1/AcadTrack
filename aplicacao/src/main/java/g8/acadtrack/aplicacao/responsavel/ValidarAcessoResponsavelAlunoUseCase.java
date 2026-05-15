package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import org.springframework.stereotype.Service;

@Service
public class ValidarAcessoResponsavelAlunoUseCase {

    public void executar(Aluno aluno, Long responsavelId, PermissaoResponsavel permissaoResponsavel) {
        aluno.validarAcessoResponsavel(responsavelId, permissaoResponsavel);
    }
}

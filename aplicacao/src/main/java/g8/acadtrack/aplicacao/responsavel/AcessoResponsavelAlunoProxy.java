package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;

public interface AcessoResponsavelAlunoProxy {

    Aluno executar(Long alunoId, Long responsavelId, PermissaoResponsavel permissaoResponsavel);
}

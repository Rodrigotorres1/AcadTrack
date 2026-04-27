package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.aplicacao.nota.AnaliseDesempenhoAcademicoResultado;
import br.com.acadtrack.aplicacao.nota.AnalisarDesempenhoAcademicoUseCase;
import br.com.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import org.springframework.stereotype.Service;

@Service
public class ConsultarDesempenhoAlunoPorResponsavelUseCase {

    private final ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;

    public ConsultarDesempenhoAlunoPorResponsavelUseCase(
            ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase
    ) {
        this.validarAcessoResponsavelAlunoUseCase = validarAcessoResponsavelAlunoUseCase;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
    }

    public AnaliseDesempenhoAcademicoResultado executar(Long responsavelId, Long alunoId) {
        validarAcessoResponsavelAlunoUseCase.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_DESEMPENHO);
        return analisarDesempenhoAcademicoUseCase.executar(alunoId);
    }
}

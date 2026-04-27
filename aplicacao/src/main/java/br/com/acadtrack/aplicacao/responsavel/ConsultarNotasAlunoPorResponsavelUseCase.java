package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.aplicacao.nota.BuscarNotasPorAlunoUseCase;
import br.com.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarNotasAlunoPorResponsavelUseCase {

    private final ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase;
    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;

    public ConsultarNotasAlunoPorResponsavelUseCase(
            ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase,
            BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase
    ) {
        this.validarAcessoResponsavelAlunoUseCase = validarAcessoResponsavelAlunoUseCase;
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
    }

    public List<Nota> executar(Long responsavelId, Long alunoId) {
        validarAcessoResponsavelAlunoUseCase.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_NOTAS);
        return buscarNotasPorAlunoUseCase.executar(alunoId);
    }
}

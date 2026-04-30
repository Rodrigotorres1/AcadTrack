package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.BuscarNotasPorAlunoUseCase;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarSimuladosAlunoPorResponsavelUseCase {

    private final ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase;
    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;

    public ConsultarSimuladosAlunoPorResponsavelUseCase(
            ValidarAcessoResponsavelAlunoUseCase validarAcessoResponsavelAlunoUseCase,
            BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase
    ) {
        this.validarAcessoResponsavelAlunoUseCase = validarAcessoResponsavelAlunoUseCase;
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
    }

    public List<Long> executar(Long responsavelId, Long alunoId) {
        validarAcessoResponsavelAlunoUseCase.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_SIMULADOS);
        return buscarNotasPorAlunoUseCase.executar(alunoId)
                .stream()
                .map(Nota::getSimuladoId)
                .distinct()
                .toList();
    }
}

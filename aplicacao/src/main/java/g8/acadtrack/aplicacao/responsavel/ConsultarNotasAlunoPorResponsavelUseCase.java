package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.BuscarNotasPorAlunoUseCase;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarNotasAlunoPorResponsavelUseCase {

    private final AcessoResponsavelAlunoService acessoResponsavelAlunoService;
    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;

    public ConsultarNotasAlunoPorResponsavelUseCase(
            AcessoResponsavelAlunoService acessoResponsavelAlunoService,
            BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase
    ) {
        this.acessoResponsavelAlunoService = acessoResponsavelAlunoService;
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
    }

    public List<Nota> executar(Long responsavelId, Long alunoId) {
        acessoResponsavelAlunoService.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_NOTAS);
        return buscarNotasPorAlunoUseCase.executar(alunoId);
    }
}

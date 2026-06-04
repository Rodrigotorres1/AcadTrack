package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.BuscarNotasEnriquecidaPorAlunoUseCase;
import g8.acadtrack.aplicacao.nota.NotaEnriquecida;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarNotasAlunoPorResponsavelUseCase {

    private final AcessoResponsavelAlunoService acessoResponsavelAlunoService;
    private final BuscarNotasEnriquecidaPorAlunoUseCase buscarNotasEnriquecidaPorAlunoUseCase;

    public ConsultarNotasAlunoPorResponsavelUseCase(
            AcessoResponsavelAlunoService acessoResponsavelAlunoService,
            BuscarNotasEnriquecidaPorAlunoUseCase buscarNotasEnriquecidaPorAlunoUseCase
    ) {
        this.acessoResponsavelAlunoService = acessoResponsavelAlunoService;
        this.buscarNotasEnriquecidaPorAlunoUseCase = buscarNotasEnriquecidaPorAlunoUseCase;
    }

    public List<NotaEnriquecida> executar(Long responsavelId, Long alunoId) {
        acessoResponsavelAlunoService.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_NOTAS);
        return buscarNotasEnriquecidaPorAlunoUseCase.executar(alunoId);
    }
}

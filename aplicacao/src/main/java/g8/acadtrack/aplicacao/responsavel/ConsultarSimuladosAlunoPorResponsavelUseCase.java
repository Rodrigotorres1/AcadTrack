package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.aplicacao.nota.BuscarNotasPorAlunoUseCase;
import g8.acadtrack.dominioacademico.aluno.PermissaoResponsavel;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarSimuladosAlunoPorResponsavelUseCase {

    private final AcessoResponsavelAlunoProxy acessoResponsavelAlunoProxy;
    private final BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase;
    private final SimuladoRepository simuladoRepository;

    public ConsultarSimuladosAlunoPorResponsavelUseCase(
            AcessoResponsavelAlunoProxy acessoResponsavelAlunoProxy,
            BuscarNotasPorAlunoUseCase buscarNotasPorAlunoUseCase,
            SimuladoRepository simuladoRepository
    ) {
        this.acessoResponsavelAlunoProxy = acessoResponsavelAlunoProxy;
        this.buscarNotasPorAlunoUseCase = buscarNotasPorAlunoUseCase;
        this.simuladoRepository = simuladoRepository;
    }

    public List<Simulado> executar(Long responsavelId, Long alunoId) {
        acessoResponsavelAlunoProxy.executar(alunoId, responsavelId, PermissaoResponsavel.VISUALIZAR_SIMULADOS);
        return buscarNotasPorAlunoUseCase.executar(alunoId)
                .stream()
                .map(Nota::getSimuladoId)
                .distinct()
                .map(simuladoRepository::buscarPorId)
                .flatMap(java.util.Optional::stream)
                .toList();
    }
}

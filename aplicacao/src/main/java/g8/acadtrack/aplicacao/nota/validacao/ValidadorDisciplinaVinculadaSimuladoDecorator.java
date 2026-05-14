package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

import java.util.List;

public class ValidadorDisciplinaVinculadaSimuladoDecorator extends ValidadorLancamentoNotaDecorator {

    private final SimuladoDisciplinaRepository simuladoDisciplinaRepository;

    public ValidadorDisciplinaVinculadaSimuladoDecorator(
            ValidadorLancamentoNota proximo,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository
    ) {
        super(proximo);
        this.simuladoDisciplinaRepository = simuladoDisciplinaRepository;
    }

    @Override
    public void validar(DadosLancamentoNota dados) {
        List<SimuladoDisciplina> disciplinasSimulado = simuladoDisciplinaRepository.buscarPorSimulado(dados.simuladoId());
        boolean disciplinaVinculada = disciplinasSimulado.stream()
                .anyMatch(item -> item.getDisciplinaId().equals(dados.disciplinaId()));

        if (!disciplinaVinculada) {
            throw new RegraDeNegocioException("Disciplina não vinculada ao simulado");
        }

        super.validar(dados);
    }
}


package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcluirSimuladoUseCase {

    private final SimuladoRepository simuladoRepository;
    private final NotaRepository notaRepository;

    public ExcluirSimuladoUseCase(SimuladoRepository simuladoRepository, NotaRepository notaRepository) {
        this.simuladoRepository = simuladoRepository;
        this.notaRepository = notaRepository;
    }

    @Transactional
    public void executar(Long simuladoId) {
        simuladoRepository.buscarPorId(simuladoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Simulado não encontrado"));

        notaRepository.excluirPorSimuladoId(simuladoId);
        simuladoRepository.excluirPorId(simuladoId);
    }
}

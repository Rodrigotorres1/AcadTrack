package g8.acadtrack.aplicacao.simulado;

import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarSimuladosUseCase {

    private final SimuladoRepository simuladoRepository;

    public ListarSimuladosUseCase(SimuladoRepository simuladoRepository) {
        this.simuladoRepository = simuladoRepository;
    }

    public List<Simulado> executar() {
        return simuladoRepository.buscarTodos();
    }
}

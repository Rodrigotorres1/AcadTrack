package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarResponsaveisUseCase {

    private final ResponsavelRepository responsavelRepository;

    public ListarResponsaveisUseCase(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public List<Responsavel> executar() {
        return responsavelRepository.buscarTodos();
    }
}

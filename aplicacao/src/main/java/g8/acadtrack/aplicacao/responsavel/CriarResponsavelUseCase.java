package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarResponsavelUseCase {

    private final ResponsavelRepository responsavelRepository;

    public CriarResponsavelUseCase(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public Responsavel executar(String nome, String email) {
        Responsavel responsavel = new Responsavel(null, nome, email);
        return responsavelRepository.salvar(responsavel);
    }
}
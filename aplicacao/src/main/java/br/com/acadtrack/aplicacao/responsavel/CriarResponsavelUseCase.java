package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import br.com.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
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
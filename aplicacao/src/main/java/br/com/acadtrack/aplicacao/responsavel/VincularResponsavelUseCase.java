package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import br.com.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;

@Service
public class VincularResponsavelUseCase {

    private final ResponsavelRepository responsavelRepository;

    public VincularResponsavelUseCase(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public void executar(Long responsavelId, String nome, String email, Long alunoId) {
        Responsavel responsavel = new Responsavel(responsavelId, nome, email);
        responsavelRepository.salvar(responsavel);
        responsavelRepository.vincularAoAluno(responsavelId, alunoId);
    }
}
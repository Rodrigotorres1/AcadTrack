package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import br.com.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DesvincularResponsavelUseCase {

    private final ResponsavelRepository responsavelRepository;

    public DesvincularResponsavelUseCase(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    public void executar(Long alunoId) {
        Optional<Responsavel> responsavelOptional = responsavelRepository.buscarPorAlunoId(alunoId);

        if (responsavelOptional.isEmpty()) {
            throw new IllegalArgumentException("Não há responsável vinculado ao aluno");
        }

        responsavelRepository.desvincularDoAluno(alunoId);
    }
}
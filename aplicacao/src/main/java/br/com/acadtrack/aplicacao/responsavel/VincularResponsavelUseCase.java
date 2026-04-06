package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import org.springframework.stereotype.Service;

@Service
public class VincularResponsavelUseCase {

    private final AlunoRepository alunoRepository;
    private final ResponsavelRepository responsavelRepository;

    public VincularResponsavelUseCase(AlunoRepository alunoRepository,
                                      ResponsavelRepository responsavelRepository) {
        this.alunoRepository = alunoRepository;
        this.responsavelRepository = responsavelRepository;
    }

    public Aluno executar(Long alunoId, Long responsavelId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Responsavel responsavel = responsavelRepository.buscarPorId(responsavelId)
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));

        aluno.vincularResponsavelId(responsavel.getId());
        return alunoRepository.salvar(aluno);
    }
}
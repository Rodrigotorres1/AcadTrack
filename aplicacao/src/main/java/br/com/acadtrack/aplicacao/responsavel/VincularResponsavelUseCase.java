package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import br.com.acadtrack.dominiousuarios.responsavel.Responsavel;
import br.com.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
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

    public Aluno executar(
            Long alunoId,
            Long responsavelId,
            boolean podeVisualizarNotas,
            boolean podeVisualizarSimulados,
            boolean podeVisualizarDesempenho
    ) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));

        Responsavel responsavel = responsavelRepository.buscarPorId(responsavelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsável não encontrado"));

        aluno.vincularResponsavel(
                responsavel.getId(),
                podeVisualizarNotas,
                podeVisualizarSimulados,
                podeVisualizarDesempenho
        );
        return alunoRepository.salvar(aluno);
    }
}

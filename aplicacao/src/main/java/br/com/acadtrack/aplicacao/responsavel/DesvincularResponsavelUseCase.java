package br.com.acadtrack.aplicacao.responsavel;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class DesvincularResponsavelUseCase {

    private final AlunoRepository alunoRepository;

    public DesvincularResponsavelUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(Long alunoId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (aluno.getResponsavelId() == null) {
            throw new IllegalStateException("Não há responsável vinculado ao aluno");
        }

        aluno.desvincularResponsavel();
        return alunoRepository.salvar(aluno);
    }
}
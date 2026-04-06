package br.com.acadtrack.aplicacao.aluno;

import br.com.acadtrack.dominioacademico.aluno.Aluno;
import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarAlunoUseCase {

    private final AlunoRepository alunoRepository;

    public CriarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno executar(String nome, String email) {
        Aluno aluno = new Aluno(null, nome, email, null);
        return alunoRepository.salvar(aluno);
    }
}
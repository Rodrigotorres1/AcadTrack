package br.com.acadtrack.aplicacao.disciplina;

import br.com.acadtrack.dominioacademico.disciplina.Disciplina;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarDisciplinaUseCase {

    private final DisciplinaRepository disciplinaRepository;

    public CriarDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public Disciplina executar(String nome) {
        Disciplina disciplina = new Disciplina(null, nome);
        return disciplinaRepository.salvar(disciplina);
    }
}
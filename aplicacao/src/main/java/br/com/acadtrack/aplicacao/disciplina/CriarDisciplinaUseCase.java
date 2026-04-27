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
        String nomeNormalizado = Disciplina.normalizarNome(nome);
        disciplinaRepository.buscarPorNomeNormalizado(nomeNormalizado)
                .ifPresent(disciplina -> {
                    throw new IllegalArgumentException("Já existe disciplina cadastrada com este nome");
                });

        Disciplina disciplina = new Disciplina(null, nome);
        return disciplinaRepository.salvar(disciplina);
    }
}
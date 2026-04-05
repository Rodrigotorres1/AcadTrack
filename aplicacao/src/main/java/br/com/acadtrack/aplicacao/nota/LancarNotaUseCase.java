package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

@Service
public class LancarNotaUseCase {

    private final NotaRepository notaRepository;

    public LancarNotaUseCase(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public Nota executar(Long alunoId, Long simuladoId, String disciplina, double valor) {
        Nota nota = new Nota(null, alunoId, simuladoId, disciplina, valor);
        return notaRepository.salvar(nota);
    }
}
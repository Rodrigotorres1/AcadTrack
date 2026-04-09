package br.com.acadtrack.aplicacao.nota;

import br.com.acadtrack.dominioacademico.aluno.AlunoRepository;
import br.com.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import br.com.acadtrack.dominioavaliacao.nota.Nota;
import br.com.acadtrack.dominioavaliacao.nota.NotaRepository;
import br.com.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class LancarNotaUseCase {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public LancarNotaUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public Nota executar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        simuladoRepository.buscarPorId(simuladoId)
                .orElseThrow(() -> new IllegalArgumentException("Simulado não encontrado"));

        disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada"));

        Nota nota = new Nota(null, alunoId, simuladoId, disciplinaId, valor);

        return notaRepository.salvar(nota);
    }
}
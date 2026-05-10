package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarNotasPorAlunoUseCase {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;

    public BuscarNotasPorAlunoUseCase(NotaRepository notaRepository, AlunoRepository alunoRepository) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
    }

    public List<Nota> executar(Long alunoId) {
        alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado"));
        return notaRepository.buscarPorAlunoId(alunoId);
    }
}
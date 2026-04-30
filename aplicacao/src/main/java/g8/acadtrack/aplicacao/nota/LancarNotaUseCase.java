package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LancarNotaUseCase {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final SimuladoRepository simuladoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;

    public LancarNotaUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.simuladoRepository = simuladoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
    }

    @Transactional
    public Nota executar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        simuladoRepository.buscarPorId(simuladoId)
                .orElseThrow(() -> new IllegalArgumentException("Simulado não encontrado"));

        Disciplina disciplina = disciplinaRepository.buscarPorId(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada"));

        if (!disciplina.estaAtiva()) {
            throw new IllegalStateException("Disciplina inativa não pode receber lançamento de nota");
        }

        if (notaRepository.existePorAlunoSimuladoDisciplina(alunoId, simuladoId, disciplinaId)) {
            throw new IllegalStateException("Já existe nota lançada para este aluno, simulado e disciplina");
        }

        Nota nota = new Nota(null, alunoId, simuladoId, disciplinaId, valor);
        Nota notaSalva = notaRepository.salvar(nota);

        double mediaAtual = avaliacaoAcademicaService.calcularMedia(notaRepository.buscarPorAlunoId(alunoId));
        aluno.atualizarDesempenhoAcademico(mediaAtual, avaliacaoAcademicaService.calcularSituacao(mediaAtual));
        alunoRepository.salvar(aluno);

        return notaSalva;
    }
}
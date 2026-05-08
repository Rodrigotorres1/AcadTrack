package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacao;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class MontarDetalheRetificacaoService {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final SimuladoRepository simuladoRepository;

    public MontarDetalheRetificacaoService(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository,
            SimuladoRepository simuladoRepository
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.simuladoRepository = simuladoRepository;
    }

    public SolicitacaoRetificacaoDetalheResultado montar(SolicitacaoRetificacao solicitacao) {
        Nota nota = notaRepository.buscarPorId(solicitacao.getNotaId()).orElse(null);
        Aluno aluno = nota == null ? null : alunoRepository.buscarPorId(nota.getAlunoId()).orElse(null);
        Disciplina disciplina = nota == null ? null : disciplinaRepository.buscarPorId(nota.getDisciplinaId()).orElse(null);
        Simulado simulado = nota == null ? null : simuladoRepository.buscarPorId(nota.getSimuladoId()).orElse(null);

        return new SolicitacaoRetificacaoDetalheResultado(
                solicitacao.getId(),
                solicitacao.getNotaId(),
                nota == null ? null : nota.getAlunoId(),
                aluno == null ? "-" : aluno.getNome(),
                nota == null ? null : nota.getDisciplinaId(),
                disciplina == null ? "-" : disciplina.getNome(),
                nota == null ? null : nota.getSimuladoId(),
                simulado == null ? "-" : simulado.getDescricao(),
                nota == null ? null : nota.getValor(),
                solicitacao.getJustificativa(),
                solicitacao.getJustificativaDecisao(),
                solicitacao.getStatus()
        );
    }
}

package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.nota.validacao.ValidacaoLancamentoNotaService;
import g8.acadtrack.aplicacao.riscoacademico.PublicadorRiscoAcademico;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LancarNotaUseCase {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final ValidacaoLancamentoNotaService validacaoLancamentoNotaService;
    private final AvaliacaoAcademicaService avaliacaoAcademicaService;
    private final AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase;
    private final PublicadorRiscoAcademico publicadorRiscoAcademico;

    public LancarNotaUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            ValidacaoLancamentoNotaService validacaoLancamentoNotaService,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            AnalisarDesempenhoAcademicoUseCase analisarDesempenhoAcademicoUseCase,
            PublicadorRiscoAcademico publicadorRiscoAcademico
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.validacaoLancamentoNotaService = validacaoLancamentoNotaService;
        this.avaliacaoAcademicaService = avaliacaoAcademicaService;
        this.analisarDesempenhoAcademicoUseCase = analisarDesempenhoAcademicoUseCase;
        this.publicadorRiscoAcademico = publicadorRiscoAcademico;
    }

    @Transactional
    public Nota executar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        Aluno aluno = validacaoLancamentoNotaService.validar(alunoId, simuladoId, disciplinaId, valor);

        Nota nota = new Nota(null, alunoId, simuladoId, disciplinaId, valor);
        Nota notaSalva = notaRepository.salvar(nota);

        double mediaAritmetica = avaliacaoAcademicaService.calcularMediaAritmetica(notaRepository.buscarPorAlunoId(alunoId));
        aluno.atualizarDesempenhoAcademico(mediaAritmetica, avaliacaoAcademicaService.calcularSituacao(mediaAritmetica));
        alunoRepository.salvar(aluno);

        AnaliseDesempenhoAcademicoResultado analise = analisarDesempenhoAcademicoUseCase.executarSemRanking(alunoId);
        publicadorRiscoAcademico.publicarSeRiscoNotificavel(analise);

        return notaSalva;
    }
}

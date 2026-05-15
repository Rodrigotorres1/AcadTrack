package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.evento.DomainEventPublisher;
import g8.acadtrack.aplicacao.nota.validacao.ValidacaoLancamentoNotaService;
import g8.acadtrack.aplicacao.riscoacademico.AnaliseRiscoAcademicoResultado;
import g8.acadtrack.aplicacao.riscoacademico.AnalisarRiscoAcademicoAlunoService;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LancarNotaUseCase {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final ValidacaoLancamentoNotaService validacaoLancamentoNotaService;
    private final AnalisarRiscoAcademicoAlunoService analisarRiscoAcademicoAlunoService;
    private final DomainEventPublisher domainEventPublisher;

    public LancarNotaUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            ValidacaoLancamentoNotaService validacaoLancamentoNotaService,
            AnalisarRiscoAcademicoAlunoService analisarRiscoAcademicoAlunoService,
            DomainEventPublisher domainEventPublisher
    ) {
        this.notaRepository = notaRepository;
        this.alunoRepository = alunoRepository;
        this.validacaoLancamentoNotaService = validacaoLancamentoNotaService;
        this.analisarRiscoAcademicoAlunoService = analisarRiscoAcademicoAlunoService;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public Nota executar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        Aluno aluno = validacaoLancamentoNotaService.validar(alunoId, simuladoId, disciplinaId, valor);

        Nota nota = new Nota(null, alunoId, simuladoId, disciplinaId, valor);
        Nota notaSalva = notaRepository.salvar(nota);

        List<Nota> notasDoAluno = notaRepository.buscarPorAlunoId(alunoId);
        AnaliseRiscoAcademicoResultado analise = analisarRiscoAcademicoAlunoService.analisar(alunoId, notasDoAluno);
        aluno.atualizarDesempenhoAcademico(analise.mediaGeral(), analise.situacaoAcademica());
        aluno.registrarRiscoAcademicoIdentificado(analise.nivelRisco());
        alunoRepository.salvar(aluno);

        domainEventPublisher.publicar(aluno.liberarEventosDominio());

        return notaSalva;
    }
}

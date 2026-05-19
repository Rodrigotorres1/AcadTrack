package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.aplicacao.nota.risco.ClassificadorRiscoAcademicoService;
import g8.acadtrack.aplicacao.ranking.ContadorParticipantesRankingPort;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalisarRiscoAcademicoSemRankingUseCase extends AnalisarDesempenhoAcademicoUseCase {

    public AnalisarRiscoAcademicoSemRankingUseCase(
            NotaRepository notaRepository,
            AlunoRepository alunoRepository,
            AvaliacaoAcademicaService avaliacaoAcademicaService,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            ContadorParticipantesRankingPort contadorParticipantesRankingPort,
            ClassificadorRiscoAcademicoService classificadorRiscoAcademicoService
    ) {
        super(
                notaRepository,
                alunoRepository,
                avaliacaoAcademicaService,
                simuladoRepository,
                disciplinaRepository,
                contadorParticipantesRankingPort,
                classificadorRiscoAcademicoService
        );
    }

    @Override
    protected boolean incluirRanking() {
        return false;
    }
}

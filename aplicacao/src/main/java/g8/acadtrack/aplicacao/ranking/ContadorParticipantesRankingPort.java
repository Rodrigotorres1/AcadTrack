package g8.acadtrack.aplicacao.ranking;

import g8.acadtrack.dominioacademico.aluno.Aluno;

import java.util.List;

public interface ContadorParticipantesRankingPort {

    List<Aluno> buscarParticipantes();

    long contarParticipantes();

    long contarParticipantesComMediaMaiorQue(double mediaGeral);
}

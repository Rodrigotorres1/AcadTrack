package g8.acadtrack.aplicacao.nota;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioavaliacao.nota.Nota;

import java.util.List;

public abstract class FluxoAnaliseAcademicaTemplate {

    public final AnaliseDesempenhoAcademicoResultado executar(Long alunoId) {
        List<Nota> notas = buscarNotas(alunoId);
        validarNotas(notas);
        double mediaGeral = calcularMediaGeral(notas);
        SituacaoAcademica situacaoAcademica = calcularSituacaoAcademica(mediaGeral);
        return montarResultado(alunoId, notas, mediaGeral, situacaoAcademica);
    }

    protected abstract List<Nota> buscarNotas(Long alunoId);

    protected void validarNotas(List<Nota> notas) {
        if (notas.isEmpty()) {
            throw new IllegalStateException("Aluno sem notas para analise de desempenho");
        }
    }

    protected abstract double calcularMediaGeral(List<Nota> notas);

    protected abstract SituacaoAcademica calcularSituacaoAcademica(double mediaGeral);

    protected abstract AnaliseDesempenhoAcademicoResultado montarResultado(
            Long alunoId,
            List<Nota> notas,
            double mediaGeral,
            SituacaoAcademica situacaoAcademica
    );
}

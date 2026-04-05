package br.com.acadtrack.dominioavaliacao.ranking;

public class ResultadoRanking {

    private final Long alunoId;
    private final double media;

    public ResultadoRanking(Long alunoId, double media) {
        this.alunoId = alunoId;
        this.media = media;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public double getMedia() {
        return media;
    }
}
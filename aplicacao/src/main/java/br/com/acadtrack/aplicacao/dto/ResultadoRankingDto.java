package br.com.acadtrack.aplicacao.dto;

public class ResultadoRankingDto {

    private Long alunoId;
    private double media;

    public ResultadoRankingDto(Long alunoId, double media) {
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
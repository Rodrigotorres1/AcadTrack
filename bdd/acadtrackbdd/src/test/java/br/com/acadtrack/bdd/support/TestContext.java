package br.com.acadtrack.bdd.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestContext {

    private final Map<String, String> vinculosAlunoTurma = new HashMap<>();
    private final Map<String, List<String>> simuladoDisciplinas = new HashMap<>();
    private final Map<String, Double> pesosDisciplinas = new HashMap<>();
    private final Map<String, Double> notasAluno = new HashMap<>();
    private final Map<String, String> responsaveisAluno = new HashMap<>();
    private final List<String> ranking = new ArrayList<>();

    private String mensagem;
    private Double mediaCalculada;
    private boolean operacaoExecutada;

    private String justificativaAtual;
    private String statusAtual;
    private String alunoAtual;
    private Long notaIdAtual;

    public Map<String, String> getVinculosAlunoTurma() {
        return vinculosAlunoTurma;
    }

    public Map<String, List<String>> getSimuladoDisciplinas() {
        return simuladoDisciplinas;
    }

    public Map<String, Double> getPesosDisciplinas() {
        return pesosDisciplinas;
    }

    public Map<String, Double> getNotasAluno() {
        return notasAluno;
    }

    public Map<String, String> getResponsaveisAluno() {
        return responsaveisAluno;
    }

    public List<String> getRanking() {
        return ranking;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Double getMediaCalculada() {
        return mediaCalculada;
    }

    public void setMediaCalculada(Double mediaCalculada) {
        this.mediaCalculada = mediaCalculada;
    }

    public boolean isOperacaoExecutada() {
        return operacaoExecutada;
    }

    public void setOperacaoExecutada(boolean operacaoExecutada) {
        this.operacaoExecutada = operacaoExecutada;
    }

    public String getJustificativaAtual() {
        return justificativaAtual;
    }

    public void setJustificativaAtual(String justificativaAtual) {
        this.justificativaAtual = justificativaAtual;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getAlunoAtual() {
        return alunoAtual;
    }

    public void setAlunoAtual(String alunoAtual) {
        this.alunoAtual = alunoAtual;
    }

    public Long getNotaIdAtual() {
        return notaIdAtual;
    }

    public void setNotaIdAtual(Long notaIdAtual) {
        this.notaIdAtual = notaIdAtual;
    }

    public void resetMensagens() {
        this.mensagem = null;
        this.mediaCalculada = null;
        this.operacaoExecutada = false;
        this.justificativaAtual = null;
        this.statusAtual = null;
        this.alunoAtual = null;
        this.notaIdAtual = null;
    }
}
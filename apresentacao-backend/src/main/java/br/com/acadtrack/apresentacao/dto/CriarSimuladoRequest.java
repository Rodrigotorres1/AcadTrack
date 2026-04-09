package br.com.acadtrack.apresentacao.dto;

import java.util.List;

public class CriarSimuladoRequest {

private String descricao;
    private List<Long> disciplinasIds;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Long> getDisciplinasIds() {
        return disciplinasIds;
    }

    public void setDisciplinasIds(List<Long> disciplinasIds) {
        this.disciplinasIds = disciplinasIds;
    }
}
package br.com.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CriarSimuladoRequest {

    @NotBlank(message = "Descrição do simulado é obrigatória")
    private String descricao;

    @NotEmpty(message = "Informe ao menos uma disciplina para o simulado")
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

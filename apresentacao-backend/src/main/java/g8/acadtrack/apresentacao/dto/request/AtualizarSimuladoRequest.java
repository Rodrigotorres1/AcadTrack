package g8.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class AtualizarSimuladoRequest {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Disciplinas são obrigatórias")
    @Size(min = 2, message = "São necessárias pelo menos 2 disciplinas")
    private List<Long> disciplinasIds;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Long> getDisciplinasIds() { return disciplinasIds; }
    public void setDisciplinasIds(List<Long> disciplinasIds) { this.disciplinasIds = disciplinasIds; }
}

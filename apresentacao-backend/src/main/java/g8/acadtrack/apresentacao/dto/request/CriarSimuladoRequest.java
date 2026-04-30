package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Nova prova modelo; inclui pelo menos uma disciplina compõe resultado.")
public class CriarSimuladoRequest {

    @Schema(example = "Simulado ENEM - demo")
    @NotBlank(message = "Descrição do simulado é obrigatória")
    private String descricao;

    @Schema(example = "[1, 2]", description = "IDs de disciplinas cadastradas no sistema")
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

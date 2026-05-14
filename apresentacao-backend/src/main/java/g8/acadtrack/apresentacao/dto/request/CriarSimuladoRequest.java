package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Nova prova modelo; inclui pelo menos duas disciplinas que compõem o resultado.")
public class CriarSimuladoRequest {

    @Schema(example = "Simulado ENEM - demo")
    @NotBlank(message = "Descrição do simulado é obrigatória")
    private String descricao;

    @Schema(example = "[1, 2]", description = "IDs de pelo menos 2 disciplinas cadastradas no sistema")
    @NotNull(message = "O simulado requer pelo menos 2 disciplinas")
    @Size(min = 2, message = "O simulado requer pelo menos 2 disciplinas")
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

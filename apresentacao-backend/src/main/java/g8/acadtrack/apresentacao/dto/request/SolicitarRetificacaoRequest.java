package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Abrir solicitação de correção pontual do valor registrado.")
public class SolicitarRetificacaoRequest {

    @Schema(description = "ID da nota a retificar")
    @NotNull(message = "Nota é obrigatória")
    private Long notaId;

    @Schema(example = "Nota inconsistente com a folha física.")
    @NotBlank(message = "Justificativa é obrigatória")
    private String justificativa;

    public SolicitarRetificacaoRequest() {
    }

    public Long getNotaId() {
        return notaId;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setNotaId(Long notaId) {
        this.notaId = notaId;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
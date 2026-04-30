package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Registro textual da decisão de indeferimento.")
public class ReprovarRetificacaoRequest {

    @Schema(example = "Ausência do comprovante citado pelo docente.")
    @NotBlank(message = "Justificativa da decisão é obrigatória")
    private String justificativaDecisao;

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }

    public void setJustificativaDecisao(String justificativaDecisao) {
        this.justificativaDecisao = justificativaDecisao;
    }
}

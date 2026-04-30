package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Decisão de ajuste quando a solicitação entra como aprovada.")
public class AprovarRetificacaoRequest {

    @Schema(example = "7.0")
    @DecimalMin(value = "0.0", message = "A nota deve estar entre 0 e 10")
    @DecimalMax(value = "10.0", message = "A nota deve estar entre 0 e 10")
    private double novoValorNota;

    @Schema(example = "Conferência com segunda correção.")
    @NotBlank(message = "Justificativa da decisão é obrigatória")
    private String justificativaDecisao;

    public double getNovoValorNota() {
        return novoValorNota;
    }

    public void setNovoValorNota(double novoValorNota) {
        this.novoValorNota = novoValorNota;
    }

    public String getJustificativaDecisao() {
        return justificativaDecisao;
    }

    public void setJustificativaDecisao(String justificativaDecisao) {
        this.justificativaDecisao = justificativaDecisao;
    }
}

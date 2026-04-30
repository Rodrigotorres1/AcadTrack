package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Peso relativo na composição média ponderada do simulado.")
public class VincularDisciplinaSimuladoRequest {

    @Schema(example = "1")
    @NotNull(message = "Disciplina é obrigatória")
    private Long disciplinaId;

    @Schema(description = "Peso (> 0)", example = "1.5")
    @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
    private double peso;

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getPeso() {
        return peso;
    }
}
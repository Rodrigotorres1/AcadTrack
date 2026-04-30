package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "CriarDisciplinaRequest", description = "Nome da nova disciplina; o servidor gera o identificador.")
public record CriarDisciplinaRequest(
        @Schema(description = "Nome legível da disciplina", example = "Matemática", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome da disciplina é obrigatório")
        String nome
) {
}
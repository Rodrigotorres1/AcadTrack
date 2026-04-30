package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Nome legível para identificar a turma.")
public class CriarTurmaRequest {

    @Schema(example = "1º ano A")
    @NotBlank(message = "Nome da turma é obrigatório")
    private String nome;

    public CriarTurmaRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
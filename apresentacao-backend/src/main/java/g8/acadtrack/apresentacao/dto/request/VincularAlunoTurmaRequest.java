package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Definição ou troca da turma de um aluno.")
public class VincularAlunoTurmaRequest {

    @Schema(description = "ID da turma", example = "1")
    @NotNull(message = "Turma é obrigatória")
    private Long turmaId;

    public VincularAlunoTurmaRequest() {
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }
}

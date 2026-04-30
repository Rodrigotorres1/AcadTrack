package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = """
        Corpo para lançar uma nota. Os campos alunoId, simuladoId e disciplinaId devem corresponder \
        aos **id devolvidos pelo sistema** nos POST anteriores (aluno, simulado, disciplina). Valores aleatórios \
        típicos (ex.: só o número 1) só funcionam se existir esse recurso na base — senão há 404.""")
public class LancarNotaRequest {

    @Schema(description = "`id` do aluno criado em `POST /alunos` (copiar do JSON da resposta 201).", example = "1")
    @NotNull(message = "Aluno é obrigatório")
    private Long alunoId;

    @Schema(description = "`id` do simulado criado em `POST /simulados`", example = "1")
    @NotNull(message = "Simulado é obrigatório")
    private Long simuladoId;

    @Schema(description = "`id` da disciplina criada em `POST /disciplinas`", example = "1")
    @NotNull(message = "Disciplina é obrigatória")
    private Long disciplinaId;

    @Schema(description = "Pontuação 0–10", example = "8.5")
    @DecimalMin(value = "0.0", message = "A nota deve estar entre 0 e 10")
    @DecimalMax(value = "10.0", message = "A nota deve estar entre 0 e 10")
    private double valor;

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public void setSimuladoId(Long simuladoId) {
        this.simuladoId = simuladoId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public Long getDisciplinaId() { 
        return disciplinaId;
    }

    public double getValor() {
        return valor;
    }
}
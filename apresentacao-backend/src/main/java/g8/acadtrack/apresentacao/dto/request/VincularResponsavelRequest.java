package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Registro de vínculo aluno/responsável e permissões consultivas.")
public class VincularResponsavelRequest {

    @Schema(description = "ID do responsável existente")
    @NotNull(message = "Responsável é obrigatório")
    private Long responsavelId;
    private boolean podeVisualizarNotas;
    private boolean podeVisualizarSimulados;
    private boolean podeVisualizarDesempenho;

    public Long getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public boolean isPodeVisualizarNotas() {
        return podeVisualizarNotas;
    }

    public void setPodeVisualizarNotas(boolean podeVisualizarNotas) {
        this.podeVisualizarNotas = podeVisualizarNotas;
    }

    public boolean isPodeVisualizarSimulados() {
        return podeVisualizarSimulados;
    }

    public void setPodeVisualizarSimulados(boolean podeVisualizarSimulados) {
        this.podeVisualizarSimulados = podeVisualizarSimulados;
    }

    public boolean isPodeVisualizarDesempenho() {
        return podeVisualizarDesempenho;
    }

    public void setPodeVisualizarDesempenho(boolean podeVisualizarDesempenho) {
        this.podeVisualizarDesempenho = podeVisualizarDesempenho;
    }

    @AssertTrue(message = "É necessário conceder ao menos uma permissão ao responsável")
    public boolean isAoMenosUmaPermissaoConcedida() {
        return podeVisualizarNotas || podeVisualizarSimulados || podeVisualizarDesempenho;
    }
}
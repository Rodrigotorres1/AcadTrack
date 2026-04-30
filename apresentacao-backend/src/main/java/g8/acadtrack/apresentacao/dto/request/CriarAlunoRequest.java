package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Cadastro inicial de um aluno (sem turma até o vínculo).")
public class CriarAlunoRequest {

    @Schema(description = "Nome completo", example = "João Silva")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "E-mail válido único para o aluno", example = "joao@example.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    public CriarAlunoRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

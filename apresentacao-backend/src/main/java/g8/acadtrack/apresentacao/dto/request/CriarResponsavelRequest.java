package g8.acadtrack.apresentacao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Cadastro inicial de um responsável (autorização aos vínculos é negociado depois).")
public class CriarResponsavelRequest {

    @Schema(example = "Maria Souza")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(example = "maria.souza@email.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
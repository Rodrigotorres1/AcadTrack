package g8.acadtrack.apresentacao.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AtualizarDisciplinaRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}

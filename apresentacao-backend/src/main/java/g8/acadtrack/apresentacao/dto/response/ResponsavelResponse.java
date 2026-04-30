package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.dominiousuarios.responsavel.Responsavel;

public class ResponsavelResponse {

    private Long id;
    private String nome;
    private String email;

    public ResponsavelResponse(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public static ResponsavelResponse fromDomain(Responsavel responsavel) {
        return new ResponsavelResponse(
                responsavel.getId(),
                responsavel.getNome(),
                responsavel.getEmail()
        );
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
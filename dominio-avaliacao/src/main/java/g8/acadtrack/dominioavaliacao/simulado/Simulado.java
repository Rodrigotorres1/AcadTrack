package g8.acadtrack.dominioavaliacao.simulado;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class Simulado {

    private Long id;
    private String descricao;

    public Simulado(Long id, String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição do simulado não pode ser vazia");
        }

        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void atualizar(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição do simulado não pode ser vazia");
        }
        this.descricao = descricao.trim();
    }
}

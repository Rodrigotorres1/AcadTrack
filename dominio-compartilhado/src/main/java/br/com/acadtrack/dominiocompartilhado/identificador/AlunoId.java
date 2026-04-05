package br.com.acadtrack.dominiocompartilhado.identificador;

public class AlunoId {

    private final Long valor;

    public AlunoId(Long valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Identificador do aluno é obrigatório");
        }
        this.valor = valor;
    }

    public Long getValor() {
        return valor;
    }
}
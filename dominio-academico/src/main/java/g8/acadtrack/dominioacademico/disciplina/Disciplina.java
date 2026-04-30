package g8.acadtrack.dominioacademico.disciplina;

public class Disciplina {

    private Long id;
    private String nome;
    private StatusDisciplina status;

    public Disciplina(Long id, String nome) {
        this(id, nome, StatusDisciplina.ATIVA);
    }

    public Disciplina(Long id, String nome, StatusDisciplina status) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da disciplina é obrigatório");
        }

        this.id = id;
        this.nome = normalizarNome(nome);
        this.status = status == null ? StatusDisciplina.ATIVA : status;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public StatusDisciplina getStatus() {
        return status;
    }

    public boolean estaAtiva() {
        return status == StatusDisciplina.ATIVA;
    }

    public void inativar() {
        this.status = StatusDisciplina.INATIVA;
    }

    public void ativar() {
        this.status = StatusDisciplina.ATIVA;
    }

    public static String normalizarNome(String nome) {
        return nome.trim().replaceAll("\\s+", " ");
    }
}

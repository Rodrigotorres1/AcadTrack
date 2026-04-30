package g8.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aluno")
public class AlunoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;
    private Long turmaId;
    private Long responsavelId;
    private boolean vinculoResponsavelAtivo;
    private boolean permissaoVisualizarNotas;
    private boolean permissaoVisualizarSimulados;
    private boolean permissaoVisualizarDesempenho;
    private double mediaAtual;
    private String situacaoAcademica;

    public AlunoJpaEntity() {
    }

    public AlunoJpaEntity(Long id, String nome, String email, Long turmaId, Long responsavelId) {
        this(id, nome, email, turmaId, responsavelId, responsavelId != null, false, false, false, 0.0, "REPROVADO");
    }

    public AlunoJpaEntity(
            Long id,
            String nome,
            String email,
            Long turmaId,
            Long responsavelId,
            boolean vinculoResponsavelAtivo,
            boolean permissaoVisualizarNotas,
            boolean permissaoVisualizarSimulados,
            boolean permissaoVisualizarDesempenho
    ) {
        this(
                id,
                nome,
                email,
                turmaId,
                responsavelId,
                vinculoResponsavelAtivo,
                permissaoVisualizarNotas,
                permissaoVisualizarSimulados,
                permissaoVisualizarDesempenho,
                0.0,
                "REPROVADO"
        );
    }

    public AlunoJpaEntity(
            Long id,
            String nome,
            String email,
            Long turmaId,
            Long responsavelId,
            boolean vinculoResponsavelAtivo,
            boolean permissaoVisualizarNotas,
            boolean permissaoVisualizarSimulados,
            boolean permissaoVisualizarDesempenho,
            double mediaAtual,
            String situacaoAcademica
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.turmaId = turmaId;
        this.responsavelId = responsavelId;
        this.vinculoResponsavelAtivo = vinculoResponsavelAtivo;
        this.permissaoVisualizarNotas = permissaoVisualizarNotas;
        this.permissaoVisualizarSimulados = permissaoVisualizarSimulados;
        this.permissaoVisualizarDesempenho = permissaoVisualizarDesempenho;
        this.mediaAtual = mediaAtual;
        this.situacaoAcademica = situacaoAcademica;
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

    public Long getTurmaId() {
        return turmaId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public boolean isVinculoResponsavelAtivo() {
        return vinculoResponsavelAtivo;
    }

    public boolean isPermissaoVisualizarNotas() {
        return permissaoVisualizarNotas;
    }

    public boolean isPermissaoVisualizarSimulados() {
        return permissaoVisualizarSimulados;
    }

    public boolean isPermissaoVisualizarDesempenho() {
        return permissaoVisualizarDesempenho;
    }

    public double getMediaAtual() {
        return mediaAtual;
    }

    public String getSituacaoAcademica() {
        return situacaoAcademica;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public void setResponsavelId(Long responsavelId) {
        this.responsavelId = responsavelId;
    }

    public void setVinculoResponsavelAtivo(boolean vinculoResponsavelAtivo) {
        this.vinculoResponsavelAtivo = vinculoResponsavelAtivo;
    }

    public void setPermissaoVisualizarNotas(boolean permissaoVisualizarNotas) {
        this.permissaoVisualizarNotas = permissaoVisualizarNotas;
    }

    public void setPermissaoVisualizarSimulados(boolean permissaoVisualizarSimulados) {
        this.permissaoVisualizarSimulados = permissaoVisualizarSimulados;
    }

    public void setPermissaoVisualizarDesempenho(boolean permissaoVisualizarDesempenho) {
        this.permissaoVisualizarDesempenho = permissaoVisualizarDesempenho;
    }

    public void setMediaAtual(double mediaAtual) {
        this.mediaAtual = mediaAtual;
    }

    public void setSituacaoAcademica(String situacaoAcademica) {
        this.situacaoAcademica = situacaoAcademica;
    }
}
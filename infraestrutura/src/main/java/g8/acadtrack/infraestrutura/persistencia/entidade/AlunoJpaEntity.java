package g8.acadtrack.infraestrutura.persistencia.entidade;

import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "turma_id")
    private Long turmaId;

    @Column(name = "responsavel_id")
    private Long responsavelId;

    private Boolean vinculoResponsavelAtivo = false;
    private Boolean permissaoVisualizarNotas = false;
    private Boolean permissaoVisualizarSimulados = false;
    private Boolean permissaoVisualizarDesempenho = false;
    private Boolean ativo = true;

    @Column(name = "media_atual")
    private double mediaAtual;

    @Enumerated(EnumType.STRING)
    private SituacaoAcademica situacaoAcademica;

    public AlunoJpaEntity() {
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
            boolean ativo,
            double mediaAritmetica,
            SituacaoAcademica situacaoAcademica
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
        this.ativo = ativo;
        this.mediaAtual = mediaAritmetica;
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
        return Boolean.TRUE.equals(vinculoResponsavelAtivo);
    }

    public boolean isPermissaoVisualizarNotas() {
        return Boolean.TRUE.equals(permissaoVisualizarNotas);
    }

    public boolean isPermissaoVisualizarSimulados() {
        return Boolean.TRUE.equals(permissaoVisualizarSimulados);
    }

    public boolean isPermissaoVisualizarDesempenho() {
        return Boolean.TRUE.equals(permissaoVisualizarDesempenho);
    }

    public boolean isAtivo() {
        return ativo == null || ativo;
    }

    public double getMediaAritmetica() {
        return mediaAtual;
    }

    public SituacaoAcademica getSituacaoAcademica() {
        return situacaoAcademica == null ? SituacaoAcademica.APROVADO : situacaoAcademica;
    }

}

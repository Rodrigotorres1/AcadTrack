package br.com.acadtrack.apresentacao.dto;

import br.com.acadtrack.dominioacademico.aluno.Aluno;

public class AlunoResponse {

    private Long id;
    private String nome;
    private String email;
    private Long turmaId;
    private Long responsavelId;
    private boolean vinculoResponsavelAtivo;
    private boolean podeVisualizarNotas;
    private boolean podeVisualizarSimulados;
    private boolean podeVisualizarDesempenho;
    private double mediaAtual;
    private String situacaoAcademica;

    public AlunoResponse(
            Long id,
            String nome,
            String email,
            Long turmaId,
            Long responsavelId,
            boolean vinculoResponsavelAtivo,
            boolean podeVisualizarNotas,
            boolean podeVisualizarSimulados,
            boolean podeVisualizarDesempenho,
            double mediaAtual,
            String situacaoAcademica
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.turmaId = turmaId;
        this.responsavelId = responsavelId;
        this.vinculoResponsavelAtivo = vinculoResponsavelAtivo;
        this.podeVisualizarNotas = podeVisualizarNotas;
        this.podeVisualizarSimulados = podeVisualizarSimulados;
        this.podeVisualizarDesempenho = podeVisualizarDesempenho;
        this.mediaAtual = mediaAtual;
        this.situacaoAcademica = situacaoAcademica;
    }

    public static AlunoResponse fromDomain(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTurmaId(),
                aluno.getResponsavelId(),
                aluno.isVinculoResponsavelAtivo(),
                aluno.isPermissaoVisualizarNotas(),
                aluno.isPermissaoVisualizarSimulados(),
                aluno.isPermissaoVisualizarDesempenho(),
                aluno.getMediaAtual(),
                aluno.getSituacaoAcademica().name()
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

    public Long getTurmaId() {
        return turmaId;
    }

    public Long getResponsavelId() {
        return responsavelId;
    }

    public boolean isVinculoResponsavelAtivo() {
        return vinculoResponsavelAtivo;
    }

    public boolean isPodeVisualizarNotas() {
        return podeVisualizarNotas;
    }

    public boolean isPodeVisualizarSimulados() {
        return podeVisualizarSimulados;
    }

    public boolean isPodeVisualizarDesempenho() {
        return podeVisualizarDesempenho;
    }

    public double getMediaAtual() {
        return mediaAtual;
    }

    public String getSituacaoAcademica() {
        return situacaoAcademica;
    }
}
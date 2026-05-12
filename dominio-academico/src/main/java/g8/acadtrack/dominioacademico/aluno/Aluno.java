package g8.acadtrack.dominioacademico.aluno;

import g8.acadtrack.dominiocompartilhado.email.Email;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

public class Aluno {

    private Long id;
    private String nome;
    private String email;
    private Long turmaId;
    private Long responsavelId;
    private boolean vinculoResponsavelAtivo;
    private boolean permissaoVisualizarNotas;
    private boolean permissaoVisualizarSimulados;
    private boolean permissaoVisualizarDesempenho;
    private boolean ativo;
    private double mediaAritmetica;
    private SituacaoAcademica situacaoAcademica;

    public Aluno(Long id, String nome, String email, Long turmaId, Long responsavelId) {
        this(
                id,
                nome,
                email,
                turmaId,
                responsavelId,
                responsavelId != null,
                false,
                false,
                false,
                0.0,
                SituacaoAcademica.REPROVADO
        );
    }

    public Aluno(
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
                SituacaoAcademica.REPROVADO
        );
    }

    public Aluno(
            Long id,
            String nome,
            String email,
            Long turmaId,
            Long responsavelId,
            boolean vinculoResponsavelAtivo,
            boolean permissaoVisualizarNotas,
            boolean permissaoVisualizarSimulados,
            boolean permissaoVisualizarDesempenho,
            double mediaAritmetica,
            SituacaoAcademica situacaoAcademica
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
                true,
                mediaAritmetica,
                situacaoAcademica
        );
    }

    public Aluno(
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
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome do aluno é obrigatório");
        }
        this.id = id;
        this.nome = nome;
        this.email = Email.normalizar(email);
        this.turmaId = turmaId;
        this.responsavelId = responsavelId;
        this.vinculoResponsavelAtivo = vinculoResponsavelAtivo;
        this.permissaoVisualizarNotas = permissaoVisualizarNotas;
        this.permissaoVisualizarSimulados = permissaoVisualizarSimulados;
        this.permissaoVisualizarDesempenho = permissaoVisualizarDesempenho;
        this.ativo = ativo;
        this.mediaAritmetica = mediaAritmetica;
        this.situacaoAcademica = situacaoAcademica == null ? SituacaoAcademica.REPROVADO : situacaoAcademica;
    }

    public void vincularTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
        if (this.turmaId != null) {
            throw new IllegalStateException("O aluno já está vinculado a uma turma");
        }
        this.turmaId = turmaId;
    }

    public void substituirTurma(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("Turma é obrigatória");
        }
        this.turmaId = turmaId;
    }

    public void vincularResponsavel(
            Long responsavelId,
            boolean podeVisualizarNotas,
            boolean podeVisualizarSimulados,
            boolean podeVisualizarDesempenho
    ) {
        if (responsavelId == null) {
            throw new IllegalArgumentException("Responsável é obrigatório");
        }

        if (!podeVisualizarNotas && !podeVisualizarSimulados && !podeVisualizarDesempenho) {
            throw new RegraDeNegocioException("É necessário conceder ao menos uma permissão ao responsável");
        }

        if (this.responsavelId != null && this.vinculoResponsavelAtivo && this.responsavelId.equals(responsavelId)) {
            throw new IllegalStateException("Já existe vínculo ativo entre aluno e responsável");
        }

        if (this.responsavelId != null && this.vinculoResponsavelAtivo && !this.responsavelId.equals(responsavelId)) {
            throw new IllegalStateException("O aluno já possui vínculo ativo com outro responsável");
        }

        this.responsavelId = responsavelId;
        this.vinculoResponsavelAtivo = true;
        this.permissaoVisualizarNotas = podeVisualizarNotas;
        this.permissaoVisualizarSimulados = podeVisualizarSimulados;
        this.permissaoVisualizarDesempenho = podeVisualizarDesempenho;
    }

    public void desvincularResponsavel() {
        if (this.responsavelId == null || !this.vinculoResponsavelAtivo) {
            throw new IllegalStateException("Não há vínculo ativo de responsável para o aluno");
        }
        this.vinculoResponsavelAtivo = false;
        this.permissaoVisualizarNotas = false;
        this.permissaoVisualizarSimulados = false;
        this.permissaoVisualizarDesempenho = false;
    }

    public void removerResponsavel() {
        this.responsavelId = null;
        this.vinculoResponsavelAtivo = false;
        this.permissaoVisualizarNotas = false;
        this.permissaoVisualizarSimulados = false;
        this.permissaoVisualizarDesempenho = false;
    }

    public void validarAcessoResponsavel(Long responsavelId, PermissaoResponsavel permissao) {
        if (responsavelId == null || this.responsavelId == null || !this.responsavelId.equals(responsavelId) || !this.vinculoResponsavelAtivo) {
            throw new IllegalStateException("Responsável sem vínculo ativo com o aluno");
        }

        boolean permitido = switch (permissao) {
            case VISUALIZAR_NOTAS -> permissaoVisualizarNotas;
            case VISUALIZAR_SIMULADOS -> permissaoVisualizarSimulados;
            case VISUALIZAR_DESEMPENHO -> permissaoVisualizarDesempenho;
        };

        if (!permitido) {
            throw new IllegalStateException("Responsável não possui permissão para acessar este recurso");
        }
    }

    public void atualizarDesempenhoAcademico(double mediaAritmetica, SituacaoAcademica situacaoAcademica) {
        this.mediaAritmetica = mediaAritmetica;
        this.situacaoAcademica = situacaoAcademica == null ? SituacaoAcademica.REPROVADO : situacaoAcademica;
    }

    public void atualizar(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("Nome do aluno é obrigatório");
        }
        this.nome = nome.trim();
        this.email = Email.normalizar(email);
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
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

    public boolean isAtivo() {
        return ativo;
    }

    public double getMediaAritmetica() {
        return mediaAritmetica;
    }

    public SituacaoAcademica getSituacaoAcademica() {
        return situacaoAcademica;
    }
}

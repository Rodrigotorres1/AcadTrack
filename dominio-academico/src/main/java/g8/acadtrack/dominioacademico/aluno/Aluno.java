package g8.acadtrack.dominioacademico.aluno;

import g8.acadtrack.dominiocompartilhado.email.Email;
import g8.acadtrack.dominiocompartilhado.evento.DomainEvent;
import g8.acadtrack.dominiocompartilhado.excecao.AcessoDenegadoException;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import g8.acadtrack.dominiocompartilhado.risco.NivelRiscoAcademico;
import g8.acadtrack.dominioacademico.aluno.evento.RiscoAcademicoEvent;

import java.util.ArrayList;
import java.util.List;

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
    private final List<DomainEvent> eventosDominio = new ArrayList<>();

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
                SituacaoAcademica.APROVADO
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
                SituacaoAcademica.APROVADO
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
        this.situacaoAcademica = situacaoAcademica == null ? SituacaoAcademica.APROVADO : situacaoAcademica;
    }

    public void substituirTurma(Long turmaId) {
        if (turmaId == null) {
            throw new RegraDeNegocioException("Turma é obrigatória");
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
            throw new RegraDeNegocioException("Responsável é obrigatório");
        }

        if (!podeVisualizarNotas && !podeVisualizarSimulados && !podeVisualizarDesempenho) {
            throw new RegraDeNegocioException("É necessário conceder ao menos uma permissão ao responsável");
        }

        if (this.responsavelId != null && this.vinculoResponsavelAtivo && this.responsavelId.equals(responsavelId)) {
            throw new ConflitoDeEstadoException("Já existe vínculo ativo entre aluno e responsável");
        }

        if (this.responsavelId != null && this.vinculoResponsavelAtivo && !this.responsavelId.equals(responsavelId)) {
            throw new ConflitoDeEstadoException("O aluno já possui vínculo ativo com outro responsável");
        }

        this.responsavelId = responsavelId;
        this.vinculoResponsavelAtivo = true;
        this.permissaoVisualizarNotas = podeVisualizarNotas;
        this.permissaoVisualizarSimulados = podeVisualizarSimulados;
        this.permissaoVisualizarDesempenho = podeVisualizarDesempenho;
    }

    public void desvincularResponsavel() {
        if (this.responsavelId == null || !this.vinculoResponsavelAtivo) {
            throw new ConflitoDeEstadoException("Não há vínculo ativo de responsável para o aluno");
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
            throw new AcessoDenegadoException("Responsável sem vínculo ativo com o aluno");
        }

        boolean permitido = switch (permissao) {
            case VISUALIZAR_NOTAS -> permissaoVisualizarNotas;
            case VISUALIZAR_SIMULADOS -> permissaoVisualizarSimulados;
            case VISUALIZAR_DESEMPENHO -> permissaoVisualizarDesempenho;
        };

        if (!permitido) {
            throw new AcessoDenegadoException("Responsável não possui permissão para acessar este recurso");
        }
    }

    public void atualizarDesempenhoAcademico(double mediaAritmetica, SituacaoAcademica situacaoAcademica) {
        this.mediaAritmetica = mediaAritmetica;
        this.situacaoAcademica = situacaoAcademica == null ? SituacaoAcademica.APROVADO : situacaoAcademica;
    }

    public void registrarRiscoAcademicoIdentificado(NivelRiscoAcademico nivelRisco) {
        if (nivelRisco == null || nivelRisco == NivelRiscoAcademico.BAIXO) {
            return;
        }

        registrarEvento(RiscoAcademicoEvent.criar(
                id,
                mediaAritmetica,
                nivelRisco,
                situacaoAcademica
        ));
    }

    public List<DomainEvent> liberarEventosDominio() {
        List<DomainEvent> eventos = List.copyOf(eventosDominio);
        eventosDominio.clear();
        return eventos;
    }

    private void registrarEvento(DomainEvent evento) {
        eventosDominio.add(evento);
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

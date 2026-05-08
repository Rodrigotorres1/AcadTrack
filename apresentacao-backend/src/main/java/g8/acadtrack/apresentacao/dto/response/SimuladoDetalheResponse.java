package g8.acadtrack.apresentacao.dto.response;

import g8.acadtrack.aplicacao.simulado.SimuladoDetalheResultado;

import java.util.List;

public class SimuladoDetalheResponse {

    private Long id;
    private String descricao;
    private boolean consistente;
    private String status;
    private String motivoInconsistencia;
    private List<DisciplinaVinculadaResponse> disciplinas;
    private NotasRelacionadasResponse notasRelacionadas;
    private List<AlunoParticipanteResponse> alunosParticipantes;

    public SimuladoDetalheResponse(
            Long id,
            String descricao,
            boolean consistente,
            String status,
            String motivoInconsistencia,
            List<DisciplinaVinculadaResponse> disciplinas,
            NotasRelacionadasResponse notasRelacionadas,
            List<AlunoParticipanteResponse> alunosParticipantes
    ) {
        this.id = id;
        this.descricao = descricao;
        this.consistente = consistente;
        this.status = status;
        this.motivoInconsistencia = motivoInconsistencia;
        this.disciplinas = disciplinas;
        this.notasRelacionadas = notasRelacionadas;
        this.alunosParticipantes = alunosParticipantes;
    }

    public static SimuladoDetalheResponse fromApplication(SimuladoDetalheResultado resultado) {
        return new SimuladoDetalheResponse(
                resultado.id(),
                resultado.descricao(),
                resultado.consistente(),
                resultado.statusConsistencia(),
                resultado.motivoInconsistencia(),
                resultado.disciplinas().stream()
                        .map(DisciplinaVinculadaResponse::fromApplication)
                        .toList(),
                NotasRelacionadasResponse.fromApplication(resultado.notasRelacionadas()),
                resultado.alunosParticipantes().stream()
                        .map(AlunoParticipanteResponse::fromApplication)
                        .toList()
        );
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isConsistente() {
        return consistente;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivoInconsistencia() {
        return motivoInconsistencia;
    }

    public List<DisciplinaVinculadaResponse> getDisciplinas() {
        return disciplinas;
    }

    public NotasRelacionadasResponse getNotasRelacionadas() {
        return notasRelacionadas;
    }

    public List<AlunoParticipanteResponse> getAlunosParticipantes() {
        return alunosParticipantes;
    }

    public static class DisciplinaVinculadaResponse {

        private Long id;
        private String nome;
        private String status;

        public DisciplinaVinculadaResponse(Long id, String nome, String status) {
            this.id = id;
            this.nome = nome;
            this.status = status;
        }

        public static DisciplinaVinculadaResponse fromApplication(SimuladoDetalheResultado.DisciplinaVinculada disciplina) {
            return new DisciplinaVinculadaResponse(
                    disciplina.id(),
                    disciplina.nome(),
                    disciplina.status()
            );
        }

        public Long getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class NotasRelacionadasResponse {

        private int totalNotas;
        private int alunosComNotas;

        public NotasRelacionadasResponse(int totalNotas, int alunosComNotas) {
            this.totalNotas = totalNotas;
            this.alunosComNotas = alunosComNotas;
        }

        public static NotasRelacionadasResponse fromApplication(SimuladoDetalheResultado.NotasRelacionadas notas) {
            return new NotasRelacionadasResponse(
                    notas.totalNotas(),
                    notas.alunosComNotas()
            );
        }

        public int getTotalNotas() {
            return totalNotas;
        }

        public int getAlunosComNotas() {
            return alunosComNotas;
        }
    }

    public static class AlunoParticipanteResponse {

        private Long alunoId;
        private String nome;
        private int quantidadeNotas;
        private double media;

        public AlunoParticipanteResponse(Long alunoId, String nome, int quantidadeNotas, double media) {
            this.alunoId = alunoId;
            this.nome = nome;
            this.quantidadeNotas = quantidadeNotas;
            this.media = media;
        }

        public static AlunoParticipanteResponse fromApplication(SimuladoDetalheResultado.AlunoParticipante aluno) {
            return new AlunoParticipanteResponse(
                    aluno.alunoId(),
                    aluno.nome(),
                    aluno.quantidadeNotas(),
                    aluno.media()
            );
        }

        public Long getAlunoId() {
            return alunoId;
        }

        public String getNome() {
            return nome;
        }

        public int getQuantidadeNotas() {
            return quantidadeNotas;
        }

        public double getMedia() {
            return media;
        }
    }
}

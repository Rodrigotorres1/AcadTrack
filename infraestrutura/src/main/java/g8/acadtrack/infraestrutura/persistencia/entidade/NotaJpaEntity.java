package g8.acadtrack.infraestrutura.persistencia.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "nota")
public class NotaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id")
    private Long alunoId;

    @Column(name = "simulado_id")
    private Long simuladoId;

    @Column(name = "disciplina_id")
    private Long disciplinaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", insertable = false, updatable = false)
    private AlunoJpaEntity aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulado_id", insertable = false, updatable = false)
    private SimuladoJpaEntity simulado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", insertable = false, updatable = false)
    private DisciplinaJpaEntity disciplina;

    private double valor;

    public NotaJpaEntity() {
    }

    public NotaJpaEntity(Long id, Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        this.id = id;
        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public Long getSimuladoId() {
        return simuladoId;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public double getValor() {
        return valor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public void setSimuladoId(Long simuladoId) {
        this.simuladoId = simuladoId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

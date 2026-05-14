package g8.acadtrack.infraestrutura.persistencia.entidade;

import g8.acadtrack.dominioacademico.disciplina.StatusDisciplina;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplina")
public class DisciplinaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private StatusDisciplina status;

    @OneToMany(mappedBy = "disciplina")
    private List<NotaJpaEntity> notas = new ArrayList<>();

    public DisciplinaJpaEntity() {
    }

    public DisciplinaJpaEntity(Long id, String nome, StatusDisciplina status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setStatus(StatusDisciplina status) {
        this.status = status;
    }
}

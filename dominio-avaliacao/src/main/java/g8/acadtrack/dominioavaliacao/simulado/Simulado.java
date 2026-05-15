package g8.acadtrack.dominioavaliacao.simulado;

import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;

import java.util.ArrayList;
import java.util.List;

public class Simulado {

    private Long id;
    private String descricao;
    private final List<SimuladoDisciplina> disciplinas;

    public Simulado(Long id, String descricao) {
        this(id, descricao, List.of());
    }

    public Simulado(Long id, String descricao, List<SimuladoDisciplina> disciplinas) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição do simulado não pode ser vazia");
        }

        this.id = id;
        this.descricao = descricao;
        this.disciplinas = new ArrayList<>();

        if (disciplinas != null) {
            disciplinas.forEach(this::adicionarDisciplinaExistente);
        }
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void atualizar(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição do simulado não pode ser vazia");
        }
        this.descricao = descricao.trim();
    }

    public SimuladoDisciplina adicionarDisciplina(Long disciplinaId, double peso) {
        if (possuiDisciplina(disciplinaId)) {
            throw new RegraDeNegocioException("Não é permitido vincular disciplina repetida no mesmo simulado");
        }

        SimuladoDisciplina disciplina = new SimuladoDisciplina(null, id, disciplinaId, peso);
        disciplinas.add(disciplina);
        return disciplina;
    }

    public void removerDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            throw new RegraDeNegocioException("Disciplina é obrigatória");
        }

        disciplinas.removeIf(disciplina -> disciplina.getDisciplinaId().equals(disciplinaId));
    }

    public boolean possuiDisciplina(Long disciplinaId) {
        if (disciplinaId == null) {
            return false;
        }

        return disciplinas.stream()
                .anyMatch(disciplina -> disciplina.getDisciplinaId().equals(disciplinaId));
    }

    public List<SimuladoDisciplina> listarDisciplinas() {
        return List.copyOf(disciplinas);
    }

    private void adicionarDisciplinaExistente(SimuladoDisciplina disciplina) {
        if (disciplina == null) {
            throw new RegraDeNegocioException("Disciplina é obrigatória");
        }

        if (id != null && disciplina.getSimuladoId() != null && !id.equals(disciplina.getSimuladoId())) {
            throw new RegraDeNegocioException("Disciplina não pertence ao simulado");
        }

        if (possuiDisciplina(disciplina.getDisciplinaId())) {
            throw new RegraDeNegocioException("Não é permitido vincular disciplina repetida no mesmo simulado");
        }

        disciplinas.add(disciplina);
    }
}

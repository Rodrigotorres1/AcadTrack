package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.aluno.Aluno;

public class DadosLancamentoNota {

    private final Long alunoId;
    private final Long simuladoId;
    private final Long disciplinaId;
    private final double valor;
    private Aluno aluno;

    public DadosLancamentoNota(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        this.alunoId = alunoId;
        this.simuladoId = simuladoId;
        this.disciplinaId = disciplinaId;
        this.valor = valor;
    }

    public Long alunoId() {
        return alunoId;
    }

    public Long simuladoId() {
        return simuladoId;
    }

    public Long disciplinaId() {
        return disciplinaId;
    }

    public double valor() {
        return valor;
    }

    public Aluno aluno() {
        return aluno;
    }

    public void definirAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}

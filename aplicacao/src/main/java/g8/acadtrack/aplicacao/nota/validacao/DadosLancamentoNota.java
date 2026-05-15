package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;

public class DadosLancamentoNota {

    private final Long alunoId;
    private final Long simuladoId;
    private final Long disciplinaId;
    private final double valor;
    private Aluno aluno;
    private Simulado simulado;

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

    public Simulado simulado() {
        return simulado;
    }

    public void definirSimulado(Simulado simulado) {
        this.simulado = simulado;
    }
}

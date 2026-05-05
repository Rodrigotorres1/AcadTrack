package g8.acadtrack.aplicacao.riscoacademico;

public record RiscoAcademicoEvent(
        Long alunoId,
        double mediaGeral,
        String nivelRisco
) {
}

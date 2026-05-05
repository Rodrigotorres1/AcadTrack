package g8.acadtrack.aplicacao.relatorio;

public enum CriterioOrdenacaoRelatorio {
    MAIOR_RISCO,
    MENOR_MEDIA,
    MELHOR_MEDIA;

    public static CriterioOrdenacaoRelatorio padraoSeNulo(CriterioOrdenacaoRelatorio criterio) {
        return criterio == null ? MAIOR_RISCO : criterio;
    }
}

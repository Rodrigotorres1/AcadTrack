Feature: Relatorio academico ordenado por criterios de desempenho

  Scenario: Gerar relatorio ordenado por maior risco academico
    Dado que existem alunos com desempenhos diferentes para relatorio academico
    Quando o sistema gerar o relatorio de desempenho academico com ordenacao "MAIOR_RISCO"
    Entao os alunos do relatorio devem aparecer do maior para o menor risco academico
    E o relatorio deve informar a quantidade de alunos analisados

  Scenario: Gerar relatorio ordenado por menor media geral
    Dado que existem alunos com desempenhos diferentes para relatorio academico
    Quando o sistema gerar o relatorio de desempenho academico com ordenacao "MENOR_MEDIA"
    Entao os alunos do relatorio devem aparecer da menor para a maior media geral

  Scenario: Gerar relatorio ordenado por melhor media geral
    Dado que existem alunos com desempenhos diferentes para relatorio academico
    Quando o sistema gerar o relatorio de desempenho academico com ordenacao "MELHOR_MEDIA"
    Entao os alunos do relatorio devem aparecer da melhor para a menor media geral

  Scenario: Usar maior risco como ordenacao padrao quando nenhum criterio for informado
    Dado que existem alunos com desempenhos diferentes para relatorio academico
    Quando o sistema gerar o relatorio de desempenho academico sem informar ordenacao
    Entao o relatorio deve usar a ordenacao "MAIOR_RISCO"
    E os alunos do relatorio devem aparecer do maior para o menor risco academico

  Scenario: Nao incluir aluno sem notas no relatorio
    Dado que existem alunos com desempenhos diferentes para relatorio academico
    Quando o sistema gerar o relatorio de desempenho academico com ordenacao "MAIOR_RISCO"
    Entao o aluno sem notas nao deve aparecer no relatorio

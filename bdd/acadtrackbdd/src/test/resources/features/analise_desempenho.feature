Feature: Análise consolidada de desempenho acadêmico

  Scenario: Gerar análise consolidada sem risco acadêmico
    Dado que o aluno "Ana Silva" possui histórico acadêmico consistente
    Quando o sistema gerar a análise consolidada de desempenho do aluno
    Então o sistema deve indicar risco acadêmico "false"
    E o nível de risco deve ser "BAIXO"

  Scenario: Gerar alerta de risco por média geral baixa
    Dado que o aluno "Bruno Lima" possui média geral baixa
    Quando o sistema gerar a análise consolidada de desempenho do aluno
    Então o sistema deve indicar risco acadêmico "true"
    E o nível de risco deve ser "ALTO"

  Scenario: Gerar alerta de risco por baixo desempenho em simulado
    Dado que o aluno "Carla Souza" possui baixo desempenho em simulado
    Quando o sistema gerar a análise consolidada de desempenho do aluno
    Então o sistema deve indicar risco acadêmico "true"
    E o nível de risco deve ser "MODERADO"

  Scenario: Não gerar análise consolidada para aluno sem notas
    Dado que o aluno "Diego Alves" não possui notas lançadas
    Quando o sistema gerar a análise consolidada de desempenho do aluno
    Então o sistema informa que o aluno está sem notas para análise

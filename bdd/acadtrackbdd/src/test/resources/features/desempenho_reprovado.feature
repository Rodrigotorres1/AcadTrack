Feature: Desempenho reprovado

  Scenario: Classificar situação acadêmica como REPROVADO para média abaixo de 5.0
    Dado que o aluno "Rafael Costa" possui média geral baixa
    Quando o sistema gerar a análise consolidada de desempenho do aluno
    Então a situação acadêmica da análise deve ser "REPROVADO"
    E o nível de risco deve ser "ALTO"

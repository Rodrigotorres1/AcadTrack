Feature: Gerar ranking

  Scenario: Gerar ranking com alunos ordenados
    Dado que existem alunos com notas lançadas no simulado
    Quando o sistema gera o ranking
    Então os alunos são ordenados do maior para o menor desempenho

  Scenario: Não gerar ranking sem notas
    Dado que não existem notas lançadas no simulado
    Quando o sistema tenta gerar o ranking
    Então o sistema informa que não há dados suficientes para gerar o ranking

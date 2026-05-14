Feature: Gerar ranking

  Scenario: Gerar ranking com alunos ordenados
    Dado que existem alunos com notas lançadas no simulado
    Quando o sistema gera o ranking
    Então os alunos são ordenados do maior para o menor desempenho

  Scenario: Não gerar ranking sem notas
    Dado que não existem notas lançadas no simulado
    Quando o sistema tenta gerar o ranking
    Então o sistema retorna um ranking vazio

  Scenario: Ranking acadêmico usa o mesmo risco da análise de desempenho
    Dado que o aluno tem situação aprovada e baixo desempenho em um simulado
    Quando o sistema gera o ranking acadêmico
    Então o nível de risco no ranking deve ser igual ao da análise de desempenho

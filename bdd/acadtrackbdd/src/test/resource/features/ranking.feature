# language: pt
Funcionalidade: Desempenho dos alunos

  Cenário: Visualizar notas já registradas
    Dado que o aluno possui notas registradas
    Quando solicitar a visualização das notas
    Então as notas do aluno devem ser exibidas com sucesso

  Cenário: Não visualizar notas quando não existirem registros
    Dado que o aluno não possui notas registradas
    Quando solicitar a visualização das notas
    Então o sistema deve informar que não existem notas registradas

  Cenário: Visualizar ranking com notas lançadas
    Dado que existem alunos com notas registradas
    Quando solicitar a visualização do ranking
    Então o ranking deve ser exibido em ordem decrescente de desempenho

  Cenário: Não visualizar ranking sem notas lançadas
    Dado que não existem notas registradas para os alunos
    Quando solicitar a visualização do ranking
    Então o sistema deve informar que não é possível gerar o ranking
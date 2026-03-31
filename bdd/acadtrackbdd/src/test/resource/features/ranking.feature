# language: pt
Funcionalidade: Consulta de desempenho

  Cenário: Aluno visualiza notas já registradas
    Dado que o aluno "João Silva" possui notas registradas
    Quando João Silva tentar visualizar suas notas
    Então o sistema exibe as notas de João Silva com sucesso

  Cenário: Aluno tenta visualizar notas sem registros
    Dado que o aluno "João Silva" não possui notas registradas
    Quando João Silva tentar visualizar suas notas
    Então o sistema informa que João Silva não possui notas registradas

  Cenário: Coordenador visualiza ranking com notas lançadas
    Dado que existem alunos com notas registradas no sistema
    Quando o coordenador tentar visualizar o ranking
    Então o sistema exibe o ranking em ordem decrescente de desempenho

  Cenário: Coordenador tenta visualizar ranking sem notas lançadas
    Dado que não existem notas registradas no sistema
    Quando o coordenador tentar visualizar o ranking
    Então o sistema informa que não é possível gerar o ranking sem notas
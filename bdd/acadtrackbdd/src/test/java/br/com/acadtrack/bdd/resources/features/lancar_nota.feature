Feature: Lançar nota

Scenario: Lançar nota válida
  Dado que o aluno "João Silva" realizou o simulado
  Quando o professor lança a nota 8.5 para o aluno
  Então o sistema registra a nota do aluno

Scenario: Não permitir nota inválida
  Dado que o aluno "João Silva" realizou o simulado
  Quando o professor lança a nota 15
  Então o sistema informa que a nota é inválida

Feature: Vincular responsável

Scenario: Vincular responsável ao aluno
  Dado que o aluno "João Silva" não possui responsável vinculado
  Quando o coordenador vincula o responsável "Maria Silva" ao aluno
  Então o sistema registra o responsável do aluno

Scenario: Não permitir desvincular responsável inexistente
  Dado que o aluno "João Silva" não possui responsável vinculado
  Quando o coordenador tenta desvincular um responsável
  Então o sistema informa que não há responsável vinculado

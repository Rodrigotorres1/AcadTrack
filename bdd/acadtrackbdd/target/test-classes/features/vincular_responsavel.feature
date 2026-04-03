Feature: Vincular ou desvincular responsável

  Scenario: Vincular responsável ao aluno
    Dado que o aluno "João Silva" não possui responsável vinculado
    Quando o coordenador vincula o responsável "Maria Silva" ao aluno "João Silva"
    Então o sistema registra o responsável "Maria Silva" para o aluno "João Silva"

  Scenario: Não permitir desvincular responsável inexistente
    Dado que o aluno "João Silva" não possui responsável vinculado
    Quando o coordenador tenta desvincular um responsável do aluno "João Silva"
    Então o sistema informa que não há responsável vinculado ao aluno

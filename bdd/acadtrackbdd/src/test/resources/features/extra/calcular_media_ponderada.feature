Feature: Calcular media por simulado

  Scenario: Calcular media com composicao padrao do simulado
    Dado que o aluno possui notas nas disciplinas da composicao padrao
    Quando o sistema calcula a media por simulado
    Entao o sistema retorna a media por simulado correta

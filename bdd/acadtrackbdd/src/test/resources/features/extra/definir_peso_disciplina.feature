Feature: Definir peso e disciplina no simulado

  Scenario: Definir peso válido para disciplina
    Dado que o simulado possui a disciplina "Matemática"
    Quando o coordenador define o peso 2 para a disciplina "Matemática"
    Então o sistema registra o peso da disciplina corretamente

  Scenario: Não permitir peso inválido
    Dado que o simulado possui a disciplina "Matemática"
    Quando o coordenador define o peso -1 para a disciplina "Matemática"
    Então o sistema informa que o peso deve ser válido

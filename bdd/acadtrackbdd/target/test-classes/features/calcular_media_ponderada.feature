Feature: Calcular média ponderada

  Scenario: Calcular média com pesos definidos
    Dado que o aluno possui notas nas disciplinas com pesos definidos
    Quando o sistema calcula a média ponderada
    Então o sistema retorna a média correta do aluno

  Scenario: Não calcular média sem pesos definidos
    Dado que existem disciplinas sem peso definido
    Quando o sistema tenta calcular a média ponderada
    Então o sistema informa que não é possível calcular a média

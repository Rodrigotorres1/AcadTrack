Feature: Recomendacao de plano de estudo por desempenho

  Scenario: Recomendar plano intensivo para aluno com risco ALTO
    Dado que o aluno "Eva Rocha" possui desempenho com risco alto para plano de estudo
    Quando o sistema recomendar o plano de estudo do aluno
    Entao o sistema deve recomendar o tipo de plano "PLANO_INTENSIVO"
    E a recomendacao deve conter nivel de risco "ALTO"

  Scenario: Recomendar plano de reforco para aluno com risco MODERADO
    Dado que o aluno "Felipe Moura" possui desempenho com risco moderado para plano de estudo
    Quando o sistema recomendar o plano de estudo do aluno
    Entao o sistema deve recomendar o tipo de plano "PLANO_REFORCO"
    E a recomendacao deve conter nivel de risco "MODERADO"

  Scenario: Recomendar plano avancado para aluno com risco BAIXO e media maior ou igual a 8
    Dado que o aluno "Gabriela Nunes" possui desempenho avancado para plano de estudo
    Quando o sistema recomendar o plano de estudo do aluno
    Entao o sistema deve recomendar o tipo de plano "PLANO_AVANCADO"
    E a recomendacao deve conter nivel de risco "BAIXO"
    E a recomendacao deve conter media geral maior ou igual a 8.0

  Scenario: Recomendar plano de manutencao para aluno com risco BAIXO e media menor que 8
    Dado que o aluno "Isabela Costa" possui desempenho de manutencao para plano de estudo
    Quando o sistema recomendar o plano de estudo do aluno
    Entao o sistema deve recomendar o tipo de plano "PLANO_MANUTENCAO"
    E a recomendacao deve conter nivel de risco "BAIXO"
    E a recomendacao deve conter media geral menor que 8.0

  Scenario: Impedir recomendacao para aluno sem notas
    Dado que o aluno "Henrique Dias" nao possui notas para plano de estudo
    Quando o sistema recomendar o plano de estudo do aluno
    Entao o sistema informa que o aluno esta sem notas para recomendacao de plano

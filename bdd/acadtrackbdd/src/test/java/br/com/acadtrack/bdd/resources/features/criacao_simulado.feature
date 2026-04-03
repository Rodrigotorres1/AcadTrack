Feature: Criar simulado

Scenario: Criar simulado com disciplinas vinculadas
  Dado que o coordenador deseja criar um simulado
  Quando ele informa as disciplinas "Matemática" e "Português"
  Então o sistema cria o simulado com as disciplinas informadas

Scenario: Não permitir criar simulado sem disciplinas
  Dado que o coordenador deseja criar um simulado
  Quando ele não informa nenhuma disciplina
  Então o sistema informa que o simulado deve possuir pelo menos uma disciplina

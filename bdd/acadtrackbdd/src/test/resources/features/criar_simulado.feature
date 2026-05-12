Feature: Criar simulado com disciplinas vinculadas

  Scenario: Criar simulado com disciplinas vinculadas
    Dado que o coordenador deseja criar um simulado
    Quando ele informa as disciplinas "Matemática" e "Português"
    Então o sistema cria o simulado com as disciplinas informadas

  Scenario: Não permitir criar simulado sem disciplinas
    Dado que o coordenador deseja criar um simulado
    Quando ele não informa nenhuma disciplina
    Então o sistema informa que o simulado deve possuir pelo menos uma disciplina

  Scenario: Não permitir criar simulado com apenas uma disciplina
    Dado que o coordenador deseja criar um simulado
    Quando ele informa apenas a disciplina "Matemática"
    Então o sistema informa que o simulado deve possuir pelo menos duas disciplinas distintas

  Scenario: Não permitir disciplina repetida na composição do simulado
    Dado que o coordenador deseja criar um simulado
    Quando ele informa a mesma disciplina "Matemática" duas vezes
    Então o sistema informa que não é permitido vincular disciplina repetida no mesmo simulado

  Scenario: Não permitir criar simulado com disciplina inexistente
    Dado que o coordenador deseja criar um simulado
    Quando ele informa uma disciplina inexistente na composição
    Então o sistema informa que uma ou mais disciplinas não existem

  Scenario: Não permitir criar simulado com descrição duplicada
    Dado que já existe um simulado com descrição "Simulado Principal"
    Quando ele tenta criar outro simulado com descrição "simulado principal"
    Então o sistema informa que já existe simulado cadastrado com esta descrição

  Scenario: Não permitir alterar disciplinas de simulado com notas lançadas
    Dado que existe um simulado com nota lancada
    Quando ele tenta alterar as disciplinas do simulado
    Então o sistema informa que simulado com notas lançadas não pode ser alterado

  Scenario: Não permitir atualizar simulado com disciplina inativa
    Dado que existe um simulado cadastrado
    Quando ele tenta atualizar o simulado com uma disciplina inativa
    Então o sistema informa que simulado nao pode ser atualizado com disciplina inativa

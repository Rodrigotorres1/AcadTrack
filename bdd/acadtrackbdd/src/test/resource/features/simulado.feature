# language: pt
Funcionalidade: Gestão de simulados

  Cenário: Cadastrar simulado com dados válidos
    Dado que o coordenador informou um título válido para o simulado
    E informou uma data válida para o simulado
    Quando solicitar o cadastro do simulado
    Então o simulado deve ser cadastrado com sucesso

  Cenário: Não cadastrar simulado sem título
    Dado que o coordenador não informou um título para o simulado
    E informou uma data válida para o simulado
    Quando solicitar o cadastro do simulado
    Então o sistema deve impedir o cadastro do simulado

  Cenário: Adicionar disciplina com peso válido ao simulado
    Dado que existe um simulado já cadastrado
    E o coordenador informou uma disciplina com peso válido
    Quando solicitar a adição da disciplina ao simulado
    Então a disciplina deve ser adicionada com sucesso

  Cenário: Não adicionar disciplina com peso inválido ao simulado
    Dado que existe um simulado já cadastrado
    E o coordenador informou uma disciplina com peso inválido
    Quando solicitar a adição da disciplina ao simulado
    Então o sistema deve impedir a adição da disciplina
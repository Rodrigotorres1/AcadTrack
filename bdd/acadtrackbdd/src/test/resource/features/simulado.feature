# language: pt
Funcionalidade: Gestão de simulados

  Cenário: Coordenador cadastra simulado com dados válidos
    Dado que o coordenador informou o título "Simulado AV1" e a data "2026-04-10"
    Quando o coordenador tentar cadastrar o simulado
    Então o sistema cadastra o simulado com sucesso

  Cenário: Coordenador tenta cadastrar simulado sem título
    Dado que o coordenador informou o título "" e a data "2026-04-10"
    Quando o coordenador tentar cadastrar o simulado
    Então o sistema informa que o título do simulado é obrigatório

  Cenário: Coordenador adiciona disciplina com peso válido ao simulado
    Dado que existe o simulado "Simulado AV1" cadastrado
    E que o coordenador informou a disciplina "Matemática" com peso 2
    Quando o coordenador tentar adicionar a disciplina ao simulado
    Então o sistema adiciona a disciplina ao simulado com sucesso

  Cenário: Coordenador tenta adicionar disciplina com peso inválido ao simulado
    Dado que existe o simulado "Simulado AV1" cadastrado
    E que o coordenador informou a disciplina "Matemática" com peso -1
    Quando o coordenador tentar adicionar a disciplina ao simulado
    Então o sistema informa que o peso da disciplina deve ser maior que zero
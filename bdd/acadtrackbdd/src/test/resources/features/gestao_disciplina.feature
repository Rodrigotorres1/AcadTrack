Feature: Gestão de disciplinas com restrições acadêmicas

  Scenario: Criar disciplina com status inicial ativa
    Quando o coordenador cria a disciplina "Matemática Aplicada"
    Então o sistema registra a disciplina como "ATIVA"

  Scenario: Não permitir disciplina duplicada por nome normalizado
    Dado que já existe a disciplina "  Matemática   Aplicada  "
    Quando o coordenador cria a disciplina "matemática aplicada"
    Então o sistema informa que já existe disciplina com esse nome

  Scenario: Impedir lançamento de nota para disciplina inativa
    Dado que existe uma disciplina inativa chamada "História"
    Quando o professor tenta lançar nota para essa disciplina inativa
    Então o sistema informa que disciplina inativa não pode receber nota

  Scenario: Impedir uso de disciplina inativa em simulado
    Dado que existe uma disciplina inativa chamada "Geografia"
    Quando o coordenador tenta criar simulado com essa disciplina inativa
    Então o sistema informa que disciplina inativa não pode ser vinculada a simulado

Feature: Validar email nos cadastros

  Scenario: Não cadastrar aluno sem email
    Quando o coordenador tenta cadastrar aluno sem e-mail
    Então o sistema informa que o e-mail é obrigatório

  Scenario: Não cadastrar aluno com email inválido
    Quando o coordenador tenta cadastrar aluno com e-mail "aluno-sem-dominio"
    Então o sistema informa que o e-mail é inválido

  Scenario: Não cadastrar responsável sem email
    Quando o coordenador tenta cadastrar responsável sem e-mail
    Então o sistema informa que o e-mail é obrigatório

  Scenario: Não cadastrar responsável com email inválido
    Quando o coordenador tenta cadastrar responsável com e-mail "responsavel@sem-dominio"
    Então o sistema informa que o e-mail é inválido

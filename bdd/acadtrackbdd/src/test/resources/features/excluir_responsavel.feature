Feature: Excluir responsavel

  Scenario: Excluir responsavel sem aluno vinculado
    Dado que existe um responsavel "Carlos Excluir" cadastrado sem aluno vinculado
    Quando o coordenador exclui o responsavel "Carlos Excluir"
    Entao o sistema confirma a exclusao do responsavel

  Scenario: Excluir responsavel com aluno vinculado limpa o vinculo do aluno
    Dado que existe um responsavel "Ana Excluir" vinculado ao aluno "Pedro Excluir"
    Quando o coordenador exclui o responsavel "Ana Excluir"
    Entao o sistema confirma a exclusao do responsavel
    E o aluno "Pedro Excluir" nao possui mais responsavel vinculado

  Scenario: Nao permitir excluir responsavel inexistente
    Dado que nao existe responsavel com o id informado para exclusao
    Quando o coordenador tenta excluir o responsavel inexistente
    Entao o sistema informa que o responsavel nao foi encontrado

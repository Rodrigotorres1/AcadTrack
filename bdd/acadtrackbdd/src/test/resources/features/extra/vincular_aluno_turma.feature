Feature: Vincular aluno à turma

  Scenario: Vincular aluno a uma turma com sucesso
    Dado que o aluno "João Silva" não está vinculado a nenhuma turma
    Quando o coordenador vincula o aluno "João Silva" à turma "3º Ano A"
    Então o sistema registra o vínculo do aluno "João Silva" à turma "3º Ano A"

  Scenario: Não permitir vincular aluno já vinculado a outra turma
    Dado que o aluno "João Silva" já está vinculado à turma "3º Ano B"
    Quando o coordenador tenta vincular o aluno "João Silva" à turma "3º Ano A"
    Então o sistema informa que o aluno já está vinculado a uma turma

  Scenario: Não permitir criar turma duplicada
    Dado que já existe uma turma chamada "Turma Duplicada"
    Quando o coordenador tenta criar outra turma chamada "TurmaDuplicada"
    Então o sistema informa que a turma já está cadastrada

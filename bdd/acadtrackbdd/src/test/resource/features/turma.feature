# language: pt
Funcionalidade: Gestão de turmas e alunos

  Cenário: Cadastrar turma com identificação válida
    Dado que o coordenador informou um nome válido para a turma
    Quando solicitar o cadastro da turma
    Então a turma deve ser cadastrada com sucesso

  Cenário: Não cadastrar turma sem identificação
    Dado que o coordenador não informou um nome para a turma
    Quando solicitar o cadastro da turma
    Então o sistema deve impedir o cadastro da turma

  Cenário: Vincular aluno a uma turma sem duplicidade
    Dado que existe uma turma cadastrada
    E existe um aluno ainda não vinculado à turma
    Quando o coordenador solicitar a vinculação do aluno à turma
    Então o aluno deve ser vinculado com sucesso

  Cenário: Não vincular aluno já vinculado à turma
    Dado que existe uma turma cadastrada
    E existe um aluno já vinculado à turma
    Quando o coordenador solicitar a vinculação do aluno à turma
    Então o sistema deve impedir a vinculação duplicada
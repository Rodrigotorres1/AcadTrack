# language: pt
Funcionalidade: Gestão de turmas e alunos

  Cenário: Coordenador cadastra turma com nome válido
    Dado que o coordenador informou o nome da turma "3A"
    Quando o coordenador tentar cadastrar a turma
    Então o sistema cadastra a turma com sucesso

  Cenário: Coordenador tenta cadastrar turma sem nome
    Dado que o coordenador informou o nome da turma ""
    Quando o coordenador tentar cadastrar a turma
    Então o sistema informa que o nome da turma é obrigatório

  Cenário: Coordenador vincula aluno ainda não vinculado à turma
    Dado que existe a turma "3A" cadastrada
    E que o aluno "João Silva" não está vinculado à turma "3A"
    Quando o coordenador tentar vincular João Silva à turma "3A"
    Então o sistema vincula João Silva à turma com sucesso

  Cenário: Coordenador tenta vincular aluno já vinculado à turma
    Dado que existe a turma "3A" cadastrada
    E que o aluno "João Silva" já está vinculado à turma "3A"
    Quando o coordenador tentar vincular João Silva à turma "3A" novamente
    Então o sistema informa que João Silva já está vinculado à turma
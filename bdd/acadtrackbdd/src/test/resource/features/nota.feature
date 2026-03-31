# language: pt
Funcionalidade: Gestão de notas

  Cenário: Lançar nota válida para aluno
    Dado que existe um aluno matriculado em uma turma
    E o professor informou uma nota válida
    Quando solicitar o lançamento da nota
    Então a nota deve ser registrada com sucesso

  Cenário: Não lançar nota fora do intervalo permitido
    Dado que existe um aluno matriculado em uma turma
    E o professor informou uma nota inválida
    Quando solicitar o lançamento da nota
    Então o sistema deve impedir o registro da nota

  Cenário: Corrigir uma nota já lançada
    Dado que existe uma nota já registrada para o aluno
    E o professor informou um novo valor válido para a nota
    Quando solicitar a correção da nota
    Então a nota deve ser atualizada com sucesso

  Cenário: Não corrigir nota inexistente
    Dado que não existe nota registrada para o aluno
    E o professor informou um novo valor válido para a nota
    Quando solicitar a correção da nota
    Então o sistema deve impedir a correção da nota
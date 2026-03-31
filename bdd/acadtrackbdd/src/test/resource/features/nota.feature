# language: pt
Funcionalidade: Gestão de notas

  Cenário: Professor lança nota válida para aluno
    Dado que o aluno "João Silva" está matriculado na turma "3A"
    E que o professor informou a nota 8.5 para João Silva
    Quando o professor tentar lançar a nota de João Silva
    Então o sistema registra a nota com sucesso

  Cenário: Professor tenta lançar nota fora do intervalo permitido
    Dado que o aluno "João Silva" está matriculado na turma "3A"
    E que o professor informou a nota 12.0 para João Silva
    Quando o professor tentar lançar a nota de João Silva
    Então o sistema informa que a nota deve estar entre 0 e 10

  Cenário: Professor corrige nota já lançada para aluno
    Dado que João Silva já possui a nota 6.0 registrada
    E que o professor informou a nova nota 7.5 para João Silva
    Quando o professor tentar corrigir a nota de João Silva
    Então o sistema atualiza a nota de João Silva com sucesso

  Cenário: Professor tenta corrigir nota inexistente
    Dado que João Silva não possui nota registrada
    E que o professor informou a nova nota 7.5 para João Silva
    Quando o professor tentar corrigir a nota de João Silva
    Então o sistema informa que não existe nota cadastrada para João Silva
Feature: Lançar nota individual

  Scenario: Lançar nota válida
    Dado que o aluno "João Silva" realizou o simulado
    Quando o professor lança a nota 8.5 para o aluno "João Silva"
    Então o sistema registra a nota do aluno

  Scenario: Não permitir nota inválida
    Dado que o aluno "João Silva" realizou o simulado
    Quando o professor lança a nota 15 para o aluno "João Silva"
    Então o sistema informa a nota deve estar entre 0 e 10

  Scenario: Não permitir nota duplicada para mesmo aluno, simulado e disciplina
    Dado que o aluno "João Silva" realizou o simulado
    E o professor já lançou a nota 7.0 para o aluno "João Silva"
    Quando o professor tenta lançar a nota 6.5 novamente para o mesmo aluno e disciplina
    Então o sistema informa que já existe nota lançada para este aluno, simulado e disciplina

  Scenario: Recalcular média automaticamente após novo lançamento
    Dado que o aluno "João Silva" possui nota 6.0 já lançada
    Quando o professor lança uma nova nota 8.0 para o aluno "João Silva" em outra disciplina
    Então o sistema atualiza a média do aluno para 7.0

  Scenario: Definir situação acadêmica no limite de recuperação (média 5.0)
    Dado que o aluno "João Silva" possui nota 4.0 já lançada
    Quando o professor lança uma nova nota 6.0 para o aluno "João Silva" em outra disciplina
    Então o sistema atualiza a situação acadêmica do aluno para "RECUPERACAO"

  Scenario: Definir situação acadêmica no limite de aprovação (média 7.0)
    Dado que o aluno "João Silva" possui nota 6.0 já lançada
    Quando o professor lança uma nova nota 8.0 para o aluno "João Silva" em outra disciplina
    Então o sistema atualiza a situação acadêmica do aluno para "APROVADO"

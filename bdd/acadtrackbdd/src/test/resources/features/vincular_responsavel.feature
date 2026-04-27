Feature: Vincular ou desvincular responsável

  Scenario: Vincular responsável ao aluno
    Dado que o aluno "João Silva" não possui responsável vinculado
    Quando o coordenador vincula o responsável "Maria Silva" ao aluno "João Silva"
    Então o sistema registra o responsável "Maria Silva" para o aluno "João Silva"

  Scenario: Não permitir duplicidade de vínculo ativo
    Dado que o aluno "João Silva" já possui vínculo ativo com responsável
    Quando o coordenador tenta vincular novamente o mesmo responsável ao aluno "João Silva"
    Então o sistema informa que já existe vínculo ativo entre aluno e responsável

  Scenario: Não permitir desvincular responsável inexistente
    Dado que o aluno "João Silva" não possui responsável vinculado
    Quando o coordenador tenta desvincular um responsável do aluno "João Silva"
    Então o sistema informa que não há vínculo ativo de responsável para o aluno

  Scenario: Impedir acesso sem vínculo ativo
    Dado que existe aluno e responsável sem vínculo ativo
    Quando o responsável tenta consultar notas do aluno sem vínculo ativo
    Então o sistema bloqueia o acesso por vínculo inativo

  Scenario: Impedir acesso sem permissão adequada
    Dado que o aluno possui vínculo ativo com responsável sem permissão para notas
    Quando o responsável tenta consultar notas do aluno sem permissão
    Então o sistema bloqueia o acesso por permissão insuficiente

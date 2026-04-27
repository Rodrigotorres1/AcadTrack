Feature: Solicitar retificação de nota

  Scenario: Solicitar retificação com justificativa válida
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação informando a justificativa "Houve erro na correção da questão discursiva"
    Então o sistema registra a solicitação de retificação com status "PENDENTE"

  Scenario: Não permitir solicitação sem justificativa
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação sem justificativa
    Então o sistema informa que a justificativa é obrigatória

  Scenario: Não permitir múltiplas solicitações de retificação em aberto para a mesma nota
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele já possui uma solicitação de retificação em aberto para essa nota
    Quando ele tenta solicitar nova retificação para a mesma nota
    Então o sistema informa que já existe solicitação de retificação em aberto para esta nota

  Scenario: Permitir transição de estado de PENDENTE para EM_ANALISE
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Revisar lançamento"
    Quando o responsável inicia a análise da solicitação de retificação
    Então o sistema atualiza a solicitação de retificação para status "EM_ANALISE"

  Scenario: Aprovar retificação atualiza nota e situação acadêmica
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Nota lançada incorretamente"
    E a solicitação está em análise
    Quando o responsável aprova a retificação alterando a nota para 9.0 com justificativa "Erro confirmado na correção"
    Então o sistema atualiza a solicitação de retificação para status "APROVADA"
    E a nota do aluno é atualizada para 9.0
    E a situação acadêmica do aluno é atualizada automaticamente

  Scenario: Reprovar retificação não altera a nota
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Quero revisão"
    E a solicitação está em análise
    Quando o responsável reprova a solicitação de retificação com justificativa "Não foi identificado erro"
    Então o sistema atualiza a solicitação de retificação para status "REPROVADA"
    E a nota do aluno permanece com o valor original
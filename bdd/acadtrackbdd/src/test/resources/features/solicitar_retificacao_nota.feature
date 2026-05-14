Feature: Solicitar retificação de nota

  Scenario: Solicitar retificação com justificativa válida
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação informando a justificativa "Houve erro na correção da questão discursiva"
    Então o sistema registra a solicitação de retificação com status "PENDENTE"

  Scenario: Não permitir solicitação sem justificativa
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação sem justificativa
    Então o sistema informa que a justificativa é obrigatória

  Scenario: Não permitir solicitar retificação para nota inexistente
    Quando ele solicita retificação para uma nota inexistente
    Então o sistema informa que a nota não foi encontrada

  Scenario: Não permitir múltiplas solicitações de retificação em aberto para a mesma nota
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele já possui uma solicitação de retificação em aberto para essa nota
    Quando ele tenta solicitar nova retificação para a mesma nota
    Então o sistema informa que já existe solicitação de retificação em aberto para esta nota

  Scenario: Não permitir iniciar análise de retificação inexistente
    Quando o responsável inicia análise de uma solicitação de retificação inexistente
    Então o sistema informa que a solicitação de retificação não foi encontrada

  Scenario: Permitir transição de estado de PENDENTE para EM_ANALISE
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Revisar lançamento"
    Quando o responsável inicia a análise da solicitação de retificação
    Então o sistema atualiza a solicitação de retificação para status "EM_ANALISE"

  Scenario: Não permitir aprovar retificação inexistente
    Quando o responsável aprova uma solicitação de retificação inexistente
    Então o sistema informa que a solicitação de retificação não foi encontrada

  Scenario: Não permitir aprovar retificação com status PENDENTE
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Nota lançada incorretamente"
    Quando o responsável aprova a retificação alterando a nota para 9.0 com justificativa "Erro confirmado na correção"
    Então o sistema informa que a solicitação deve estar em análise para aprovação
    E a solicitação permanece com status "PENDENTE"

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

  Scenario: Não permitir reprovar retificação inexistente
    Quando o responsável reprova uma solicitação de retificação inexistente
    Então o sistema informa que a solicitação de retificação não foi encontrada

  Scenario: Não permitir reprovar retificação já aprovada
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Nota lançada incorretamente"
    E a solicitação está em análise
    E o responsável aprova a retificação alterando a nota para 9.0 com justificativa "Erro confirmado na correção"
    Quando o responsável reprova a solicitação de retificação com justificativa "Tentativa fora de estado"
    Então o sistema informa que a solicitação deve estar em análise para reprovação
    E a solicitação permanece com status "APROVADA"

  Scenario: Não permitir aprovar retificação sem justificativa de decisão
    Dado que o aluno "Maria Oliveira" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Revisão necessária"
    E a solicitação está em análise
    Quando o responsável tenta aprovar a retificação sem justificativa de decisão
    Então o sistema informa que a justificativa da decisão é obrigatória

  Scenario: Não permitir reprovar retificação sem justificativa de decisão
    Dado que o aluno "Pedro Santos" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Revisão necessária"
    E a solicitação está em análise
    Quando o responsável tenta reprovar a retificação sem justificativa de decisão
    Então o sistema informa que a justificativa da decisão é obrigatória

  Scenario: Permitir aluno inativo solicitar retificação de nota existente
    Dado que o aluno "João Silva" possui uma nota lançada
    E o aluno está inativo
    Quando ele solicita retificação informando a justificativa "Revisar nota lançada antes da inativação"
    Então o sistema registra a solicitação de retificação com status "PENDENTE"

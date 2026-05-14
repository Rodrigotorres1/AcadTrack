Feature: Guards de retificação

  Scenario: Impedir transição de solicitação APROVADA para EM_ANALISE
    Dado que o aluno "João Silva" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Nota lançada incorretamente"
    E a solicitação está em análise
    E o responsável aprova a retificação alterando a nota para 9.0 com justificativa "Erro confirmado na correção"
    Quando o responsável inicia a análise da solicitação de retificação
    Então o sistema informa que a solicitação deve estar pendente para iniciar análise
    E a solicitação permanece com status "APROVADA"

  Scenario: Impedir transição de solicitação REPROVADA para EM_ANALISE
    Dado que o aluno "Maria Oliveira" possui uma nota lançada
    E ele solicita retificação informando a justificativa "Quero revisão"
    E a solicitação está em análise
    E o responsável reprova a solicitação de retificação com justificativa "Não foi identificado erro"
    Quando o responsável inicia a análise da solicitação de retificação
    Então o sistema informa que a solicitação deve estar pendente para iniciar análise
    E a solicitação permanece com status "REPROVADA"

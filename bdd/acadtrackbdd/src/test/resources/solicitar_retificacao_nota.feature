Feature: Solicitar retificação de nota

  Scenario: Solicitar retificação com justificativa
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação informando uma justificativa
    Então o sistema registra a solicitação de retificação

  Scenario: Não permitir solicitação sem justificativa
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação sem justificativa
    Então o sistema informa que a justificativa é obrigatória

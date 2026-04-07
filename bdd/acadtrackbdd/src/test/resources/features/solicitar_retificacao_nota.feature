Feature: Solicitar retificação de nota

  Scenario: Solicitar retificação com justificativa válida
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação informando a justificativa "Houve erro na correção da questão discursiva"
    Então o sistema registra a solicitação de retificação com status "PENDENTE"

  Scenario: Não permitir solicitação sem justificativa
    Dado que o aluno "João Silva" possui uma nota lançada
    Quando ele solicita retificação sem justificativa
    Então o sistema informa que a justificativa é obrigatória
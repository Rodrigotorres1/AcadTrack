Feature: Notificacao automatica de responsaveis sobre risco academico

  Scenario: Criar notificacao para responsavel quando aluno entrar em risco ALTO
    Dado que o aluno "Lucas Barreto" possui responsavel vinculado para notificacao
    Quando o professor lanca notas que geram risco academico ALTO
    Entao o responsavel deve receber notificacao com nivel de risco "ALTO" e prioridade "ALTA"
    E a notificacao deve estar com status "NAO_LIDA"

  Scenario: Criar notificacao para responsavel quando aluno entrar em risco MODERADO
    Dado que o aluno "Marina Teixeira" possui responsavel vinculado para notificacao
    Quando o professor lanca notas que geram risco academico MODERADO
    Entao o responsavel deve receber notificacao com nivel de risco "MODERADO" e prioridade "MEDIA"
    E a notificacao deve estar com status "NAO_LIDA"

  Scenario: Nao criar notificacao quando aluno permanecer com risco BAIXO
    Dado que o aluno "Nicolas Freire" possui responsavel vinculado para notificacao
    Quando o professor lanca notas que mantem risco academico BAIXO
    Entao o responsavel nao deve receber notificacao de risco academico

  Scenario: Nao falhar quando aluno em risco nao tiver responsavel vinculado
    Dado que o aluno "Olivia Ramos" nao possui responsavel vinculado para notificacao
    Quando o professor lanca notas que geram risco academico ALTO
    Entao o lancamento de notas em risco academico deve ser concluido sem falha

  Scenario: Listar notificacoes de um responsavel
    Dado que o aluno "Paulo Mendes" possui responsavel vinculado para notificacao
    Quando o professor lanca notas que geram risco academico ALTO
    E o responsavel consulta suas notificacoes
    Entao o sistema deve listar as notificacoes do responsavel

# Padroes de projeto na Entrega 2

O uso dos padroes abaixo foi mantido ligado aos fluxos existentes do AcadTrack. A decisao foi evitar criar funcionalidades artificiais apenas para encaixar padroes.

| Padrao | Onde aparece no codigo | Funcionalidade | Problema resolvido | Uso natural |
|---|---|---|---|---|
| Domain Events | `DomainEvent`, `RiscoAcademicoEvent`, `DomainEventPublisher`, `NotificarResponsavelRiscoAcademicoHandler` | Lancamento de notas, analise de desempenho e notificacoes | Quando a nota/desempenho muda, publicar evento de dominio sem acoplar agregados e casos de uso ao repositorio de notificacoes | Sim: notificacao e consequencia do evento academico |
| Iterator | `RankingAcademicoIterator`, `ListaRankingAcademicoIterator`, `GerarRankingAcademicoUseCase` | Ranking academico de apoio a analise | Percorrer lista ordenada de alunos sem expor a estrutura interna da colecao | Sim: ranking precisa ser percorrido em ordem |
| Template Method | `FluxoAnaliseAcademicaTemplate` e `AnalisarDesempenhoAcademicoUseCase` | Analise consolidada de desempenho | Padronizar o fluxo: buscar notas, validar, calcular media, definir situacao e montar resultado | Sim: toda analise segue os mesmos passos |
| Decorator | `ValidadorLancamentoNotaDecorator` e decoradores de valor, entidades, disciplina ativa e duplicidade | Lancamento de notas | Adicionar validacoes extras sem concentrar tudo no caso de uso principal | Sim: cada regra complementa a validacao anterior |
| Proxy | `AcessoResponsavelAlunoProxy` e `ValidarAcessoResponsavelAlunoUseCase` | Portal do responsavel e consultas autorizadas | Intermediar acesso do responsavel aos dados do aluno, validando vinculo e permissoes | Sim: responsavel nao acessa dados diretamente |
| Strategy | `EstrategiaClassificacaoRiscoAcademico`, `RiscoAltoStrategy`, `RiscoModeradoStrategy`, `RiscoBaixoStrategy`, `ClassificadorRiscoAcademicoService` | Classificacao de risco academico | Separar criterios de classificacao de risco, permitindo troca/expansao das regras | Sim: classificacao varia por criterio academico |

## Relacao com as funcionalidades existentes

- **Lancamento de notas:** usa Decorator para validar, atualiza media/situacao e publica Domain Event.
- **Analise de desempenho:** usa Template Method no fluxo padrao, Strategy para nivel de risco e Iterator para ranking/posicao.
- **Responsaveis e portal:** usa Proxy para proteger consultas de notas, simulados e desempenho.
- **Notificacoes:** usa handler de Domain Event como incremento dos fluxos de notas/desempenho, nao como funcionalidade independente.
- **Ranking:** permanece apoio da analise academica, nao substitui o relatorio removido.

## Pontos de cuidado

- O plano de estudo por desempenho foi removido.
- O relatorio academico com filtros foi removido como funcionalidade principal.
- O ranking nao reintroduz a aba Relatorio; ele serve para Top 10, posicao do aluno e notificacao de destaque.

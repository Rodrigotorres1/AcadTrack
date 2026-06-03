# Padroes de projeto na Entrega 2

O uso dos padroes abaixo foi mantido ligado aos fluxos existentes do AcadTrack. A decisao foi evitar criar funcionalidades artificiais apenas para encaixar padroes.

| Padrao | Onde aparece no codigo | Funcionalidade | Problema resolvido | Uso natural |
|---|---|---|---|---|
| Factory | `SolicitacaoRetificacaoFabrica` (`dominio-avaliacao/retificacao/`) | Solicitacao de retificacao de nota | Centraliza a criacao de novas solicitacoes, garantindo que o estado inicial PENDENTE e a ausencia de justificativa de decisao sejam invariantes do dominio — nao responsabilidade do chamador | Sim: toda nova solicitacao nasce PENDENTE sem justificativa de decisao |
| Domain Events | `DomainEvent`, `RiscoAcademicoEvent`, `DomainEventPublisher`, `NotificarResponsavelRiscoAcademicoHandler` | Lancamento de notas, analise de desempenho e notificacoes | Quando a nota/desempenho muda, publicar evento de dominio sem acoplar agregados e casos de uso ao repositorio de notificacoes | Sim: notificacao e consequencia do evento academico |
| Template Method | `FluxoAnaliseAcademicaTemplate` e `AnalisarDesempenhoAcademicoUseCase` | Analise consolidada de desempenho | Padronizar o fluxo: buscar notas, validar, calcular media, definir situacao e montar resultado | Sim: toda analise segue os mesmos passos |
| Decorator | `ValidadorLancamentoNotaDecorator` e decoradores de valor, entidades, disciplina ativa e duplicidade | Lancamento de notas | Adicionar validacoes extras sem concentrar tudo no caso de uso principal | Sim: cada regra complementa a validacao anterior |
| Proxy | `AcessoResponsavelAlunoProxy` e `ValidarAcessoResponsavelAlunoUseCase` | Portal do responsavel e consultas autorizadas | Intermediar acesso do responsavel aos dados do aluno, validando vinculo e permissoes | Sim: responsavel nao acessa dados diretamente |
| Strategy | `EstrategiaClassificacaoRiscoAcademico`, `RiscoAltoStrategy`, `RiscoModeradoStrategy`, `RiscoBaixoStrategy`, `ClassificadorRiscoAcademicoService` | Classificacao de risco academico | Separar criterios de classificacao de risco, permitindo troca/expansao das regras | Sim: classificacao varia por criterio academico |
| Iterator | `RankingAcademicoIterator`, `ListaRankingAcademicoIterator`, `GerarRankingAcademicoUseCase` | Geracao de ranking | Percorrer a colecao ordenada com limite sem expor o indice interno | Sim: ranking tem limite configuravel |

## Por que Factory para SolicitacaoRetificacao

O construtor de `SolicitacaoRetificacao` aceita todos os campos incluindo `id`, `justificativaDecisao` e `status`. Isso e necessario para reconstituir objetos carregados do banco de dados, onde todos esses valores ja existem.

Ao criar uma nova solicitacao, porem, tres dessas decisoes sao sempre fixas:
- `id = null` (ainda nao persistido)
- `justificativaDecisao = null` (decisao ainda nao tomada)
- `status = PENDENTE` (estado inicial obrigatorio)

Sem a fabrica, o chamador precisaria conhecer esse invariante:
```java
// antes — conhecimento de dominio vazando para o caso de uso
new SolicitacaoRetificacao(null, notaId, justificativa, null, StatusSolicitacaoRetificacao.PENDENTE)
```

Com a fabrica, o invariante de criacao pertence ao dominio:
```java
// depois — intenção clara, invariante protegido
SolicitacaoRetificacaoFabrica.nova(notaId, justificativa)
```

A separacao entre **criacao** (`SolicitacaoRetificacaoFabrica.nova`) e **reconstituicao** (construtor completo via repositorio) e o beneficio central do padrao Factory em DDD.

## Relacao com as funcionalidades existentes

- **Solicitacao de retificacao:** usa Factory para criar novas solicitacoes garantindo estado inicial correto.
- **Lancamento de notas:** usa Decorator para validar, atualiza media/situacao e publica Domain Event.
- **Analise de desempenho:** usa Template Method no fluxo padrao, Strategy para nivel de risco e Iterator para limitar o ranking.
- **Responsaveis e portal:** usa Proxy para proteger consultas de notas, simulados e desempenho.
- **Notificacoes:** usa handler de Domain Event como incremento dos fluxos de notas/desempenho, nao como funcionalidade independente.
- **Ranking:** permanece apoio da analise academica, nao substitui o relatorio removido.

## Pontos de cuidado

- O plano de estudo por desempenho foi removido.
- O relatorio academico com filtros foi removido como funcionalidade principal.
- O ranking nao reintroduz a aba Relatorio; ele serve para Top 10, posicao do aluno e notificacao de destaque.

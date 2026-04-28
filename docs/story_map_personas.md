# Story Map com Personas

Este story map organiza as funcionalidades por personas e releases.

## Personas

- **Coordenador**: foco em gestao academica e organizacao de simulados.
- **Professor**: foco em avaliacao, lancamento e revisao de notas.
- **Aluno**: foco em acompanhamento de desempenho e solicitacao de retificacao.
- **Responsavel**: foco em consulta supervisionada com permissoes.

> Nota da entrega atual: o **Coordenador** esta modelado como persona de negocio, mas ainda nao existe como role tecnica de autenticacao/autorizacao no backend.

## Story Map

| Persona | Atividade | Release 1 (implementado) | Release 2 (futuro) |
|---|---|---|---|
| Coordenador | Gerenciar disciplinas | Cadastrar disciplina, evitar duplicidade, inativar/ativar para uso academico | Historico de mudancas, regras por periodo letivo, trilha de auditoria |
| Coordenador | Gerenciar simulados | Criar simulado com composicao valida, bloquear disciplina inexistente/repetida, validar descricao duplicada | Versao de simulado, janela de aplicacao, consistencia global de pesos |
| Coordenador | Gerenciar vinculacoes academicas | Vincular aluno a turma e organizar fluxo academico basico | Regras por periodo, bloqueios avancados de alocacao, validacao de capacidade por turma |
| Professor | Lancar e validar notas | Lancar nota (0 a 10), bloquear duplicidade, atualizar media/situacao automaticamente | Lancamento em lote, importacao por planilha, politicas de arredondamento |
| Professor | Tratar retificacao | Iniciar analise, aprovar/reprovar com justificativa, atualizar nota e desempenho | SLA de analise, anexos/evidencias, auditoria detalhada de decisoes |
| Aluno | Solicitar retificacao | Abrir solicitacao com justificativa obrigatoria, impedir solicitacoes em aberto duplicadas | Acompanhar timeline da solicitacao, notificacoes de status, historico pessoal |
| Aluno | Acompanhar desempenho | Visualizar resultado consolidado e risco academico (via fluxos existentes) | Recomendacoes personalizadas, metas de recuperacao, graficos de evolucao |
| Responsavel | Consultar informacoes do aluno | Consultar notas, simulados e desempenho com vinculo/permissoes validos | Permissoes granulares por recurso, alertas proativos, relatorios comparativos |

## Backlog sugerido por prioridade

1. Implementar controle de acesso por perfil (incluindo Coordenador como role tecnica).
2. Regras globais de consistencia de pesos no simulado.
3. Auditoria e historico para retificacao, notas e alteracoes academicas.
4. Dashboard analitico por turma, disciplina e periodo.

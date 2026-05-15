# Módulo `apresentacao-frontend`

Este módulo Maven existe como **placeholder conceitual** (`packaging=pom`, sem código Java compilável). Ele representa a camada de apresentação web dentro da arquitetura multi-módulo do AcadTrack.

---

## Por que existe e por que não tem código aqui

O Spring Boot serve arquivos estáticos automaticamente a partir de:

```
apresentacao-backend/src/main/resources/static/
```

Essa é a localização padrão do Spring — nenhuma configuração extra é necessária. Os três arquivos da SPA ficam lá:

```
static/
├── index.html   Interface web (login por persona, navegação lateral, formulários)
├── styles.css   Estilos da interface
└── app.js       Lógica da SPA: chamadas REST, gerenciamento de estado, navegação
```

A pasta `apresentacao-frontend/static/` é uma cópia de referência dos mesmos arquivos, mantida para deixar claro na estrutura de pastas que existe uma camada de frontend separada conceitualmente. **A versão que o Spring Boot serve é sempre a que está em `apresentacao-backend/static/`.**

---

## O que a interface faz

A SPA consome a API REST do backend via `fetch`. Não acessa banco de dados, repositórios ou regras de negócio diretamente.

Funcionalidades disponíveis na interface:

| Seção | O que permite fazer |
|---|---|
| Login | Selecionar persona (Coordenador, Professor, Aluno, Responsável) |
| Alunos | Cadastrar, listar, editar, ativar/inativar, vincular à turma e a responsável |
| Turmas | Cadastrar e listar |
| Disciplinas | Cadastrar, editar, ativar/inativar, excluir |
| Simulados | Criar com composição de disciplinas, listar, detalhar |
| Notas | Lançar nota (com seleção de aluno, simulado e disciplina), listar por aluno |
| Desempenho | Consultar análise consolidada de desempenho de um aluno |
| Ranking | Visualizar ranking acadêmico geral |
| Retificações | Solicitar retificação, iniciar análise, aprovar ou reprovar |
| Responsáveis | Cadastrar, listar, excluir |
| Notificações | Listar notificações de um responsável e marcar como lida |
| Portal do responsável | Consultar notas, simulados e desempenho de aluno vinculado |

---

## Como acessar

Com o backend rodando:

```
http://localhost:8080/
```

(ou na porta alternativa configurada em `application.properties` / pelo script `scripts/run-backend.ps1`)

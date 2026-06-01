# Módulo `apresentacao-frontend`

Este módulo Maven (`packaging=jar`) contém todos os arquivos da camada de apresentação web do AcadTrack.

---

## Estrutura

```
src/main/resources/static/
├── index.html        Interface web (login por persona, navegação lateral, formulários)
├── styles.css        Estilos da interface
├── app.js            Lógica principal da SPA: chamadas REST, gerenciamento de estado, navegação
└── js/
    ├── apiClient.js  Cliente HTTP centralizado para chamadas à API REST
    ├── config.js     Configurações globais (base URL, etc.)
    ├── errors.js     Tratamento de erros da API
    ├── navigation.js Roteamento e navegação entre seções
    ├── session.js    Gerenciamento de sessão e persona ativa
    ├── store.js      Estado global da aplicação
    ├── utils.js      Funções utilitárias compartilhadas
    └── views/
        └── ui.js     Funções de renderização de componentes visuais
```

O Spring Boot serve os arquivos automaticamente a partir do classpath (`classpath:/static/`). Como `apresentacao-frontend` é uma dependência do `apresentacao-backend`, seus recursos ficam no classpath do jar executável — nenhuma configuração extra é necessária.

---

## O que a interface faz

A SPA consome a API REST do backend via `fetch`. Não acessa banco de dados, repositórios ou regras de negócio diretamente.

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

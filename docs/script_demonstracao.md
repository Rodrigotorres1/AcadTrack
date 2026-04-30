# Script de Demonstracao da API (Swagger/Postman)

Este roteiro foi pensado para apresentacao da 1a entrega com foco nas 6 funcionalidades de alta complexidade.

## 1) Preparacao rapida

- Subir backend:
  - `mvn clean install`
  - `mvn spring-boot:run -pl apresentacao-backend` (porta **8080** por defeito; outra porta: `-Dspring-boot.run.arguments=--server.port=9001`)
- Abrir Swagger:
  - `http://localhost:8080/swagger-ui/index.html`

Sugestao: execute os passos na ordem abaixo para reduzir retrabalho com IDs.

Importante:
- Preferencialmente use banco limpo antes da demo.
- Se houver dados anteriores, os IDs podem variar e os exemplos com IDs devem sempre usar os valores retornados nas respostas anteriores.

### Como interpretar as respostas (HTTP)

| Situação | Código típico | O que verificar |
|----------|---------------|-----------------|
| Criação bem-sucedida | **201 Created** | Corpo com o recurso gerado; guarde o `id` para os próximos passos. |
| Leitura ou operação sem mudança de recurso | **200 OK** | Corpo conforme o endpoint (lista, detalhe, etc.). |
| Requisição inválida (JSON, campos faltando, regra de negócio) | **400 Bad Request** | Mensagem de erro; confira o body do Swagger e envie só os campos do DTO. |
| Recurso não existe | **404 Not Found** | Confirme o `id` usado e se a ordem do roteiro foi respeitada. |
| Conflito (ex.: duplicidade, estado inválido) | **409 Conflict** | Ajuste dados ou ordem do fluxo. |

Dica: no PowerShell, escape aspas em JSON (`\"`) ou use um arquivo `.json` com `curl -d @arquivo.json` para evitar corpo inválido e **400**.

## 2) Dados base (usar no inicio)

1. `POST /disciplinas`
```json
{
  "nome": "Matematica Demo"
}
```
Guarde o `id` retornado como `disciplinaMatematicaId`.

2. `POST /disciplinas`
```json
{
  "nome": "Portugues Demo"
}
```
Guarde o `id` retornado como `disciplinaPortuguesId`.

3. `POST /alunos`
```json
{
  "nome": "Joao Demo",
  "email": "joao.demo@email.com"
}
```
Guarde o `id` retornado como `alunoId`.

4. `POST /responsaveis`
```json
{
  "nome": "Maria Demo",
  "email": "maria.demo@email.com"
}
```
Guarde o `id` retornado como `responsavelId`.

5. `PUT /alunos/{alunoId}/responsavel`
```json
{
  "responsavelId": "<usar responsavelId retornado na resposta anterior>",
  "podeVisualizarNotas": true,
  "podeVisualizarSimulados": true,
  "podeVisualizarDesempenho": true
}
```

## 3) Funcionalidade 1 - Gestao de disciplinas

### Fluxo positivo
- Validar disciplina criada com `status = ATIVA`.
- `DELETE /disciplinas/{disciplinaId}` (inativacao logica).
- `PATCH /disciplinas/{disciplinaId}/ativar` (reativacao).

### Fluxo negativo
- Tentar criar disciplina duplicada por nome normalizado (esperado: 409).

## 4) Funcionalidade 2 - Gestao de responsaveis

### Fluxo positivo
- Vinculo com permissoes via `PUT /alunos/{alunoId}/responsavel`.
- Consulta por responsavel:
  - `GET /responsaveis/{responsavelId}/alunos/{alunoId}/notas`
  - `GET /responsaveis/{responsavelId}/alunos/{alunoId}/simulados`
  - `GET /responsaveis/{responsavelId}/alunos/{alunoId}/desempenho`

### Fluxo negativo
- Repetir vinculo ativo para mesmo aluno/responsavel (esperado: 409).

## 5) Funcionalidade 5 - Criacao inteligente de simulados

1. `POST /simulados`
```json
{
  "descricao": "Simulado Demo Principal",
  "disciplinasIds": [
    "<usar disciplinaMatematicaId>",
    "<usar disciplinaPortuguesId>"
  ]
}
```
Guarde o `id` retornado como `simuladoId`.

### Fluxos negativos recomendados
- Composicao vazia (esperado: 400).
- Apenas 1 disciplina (esperado: 400).
- Disciplina repetida na lista (esperado: 400).
- Disciplina inexistente (esperado: 404).
- Descricao duplicada (esperado: 409).

## 6) Funcionalidade 3 - Lancamento de notas + situacao automatica

1. `POST /notas`
```json
{
  "alunoId": "<usar alunoId>",
  "simuladoId": "<usar simuladoId>",
  "disciplinaId": "<usar disciplinaMatematicaId>",
  "valor": 6.0
}
```
Guarde o `id` retornado como `notaMatematicaId`.

2. `POST /notas`
```json
{
  "alunoId": "<usar alunoId>",
  "simuladoId": "<usar simuladoId>",
  "disciplinaId": "<usar disciplinaPortuguesId>",
  "valor": 8.0
}
```
Guarde o `id` retornado como `notaPortuguesId`.

Validar no `AlunoResponse`:
- `mediaAtual` (arredondada com 2 casas)
- `situacaoAcademica`

### Fluxos negativos recomendados
- Nota fora de 0..10 (esperado: 400).
- Duplicidade por (aluno, simulado, disciplina) (esperado: 409).

## 7) Funcionalidade 4 - Analise consolidada de desempenho

- `GET /responsaveis/{responsavelId}/alunos/{alunoId}/desempenho`

Validar retorno:
- `mediaGeral`
- `historicoSimulados`
- `riscoAcademico`
- `nivelRisco`
- `alerta`

## 8) Funcionalidade 6 - Fluxo de retificacao com decisao

Pre-requisito obrigatorio para retificacao:
- Aluno, disciplinas, simulado e notas ja criados nos passos anteriores.
- Use o `notaId` retornado no lancamento de nota (ex.: `notaMatematicaId`).

1. Abrir solicitacao:
   - `POST /retificacoes`
```json
{
  "notaId": "<usar notaMatematicaId>",
  "justificativa": "Erro na correcao da questao 3"
}
```
Guarde o `id` retornado como `solicitacaoId`.

2. Iniciar analise:
   - `PATCH /retificacoes/{solicitacaoId}/em-analise`

3. Aprovar com justificativa de decisao:
   - `PATCH /retificacoes/{solicitacaoId}/aprovar`
```json
{
  "novoValorNota": 9.0,
  "justificativaDecisao": "Revisao confirmou erro de correcao"
}
```

4. Reprovar (em outro caso):
   - `PATCH /retificacoes/{solicitacaoId}/reprovar`
```json
{
  "justificativaDecisao": "Nao foi identificado erro material"
}
```

### Validacoes esperadas
- Nao permitir justificativa vazia na abertura (400).
- Nao permitir multiplas solicitacoes em aberto para mesma nota (409).
- Maquina de estados respeitada:
  - `PENDENTE -> EM_ANALISE -> APROVADA|REPROVADA`
- Ao aprovar:
  - nota alterada
  - media/situacao recalculadas automaticamente

## 9) Checklist final da apresentacao

- Swagger mostrando contratos finais.
- BDD/Cucumber verde.
- Mensagens e status HTTP coerentes (400/404/409).
- Controllers sem regra de negocio (somente orquestracao).
- Foco da demo mantido nas 6 funcionalidades, com exemplos de fluxo positivo e negativo.

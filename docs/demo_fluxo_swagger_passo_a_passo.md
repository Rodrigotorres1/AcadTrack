# Fluxo pré-pronto para o Swagger («Try it out»)

Faça nesta ordem. Em cada passo, **Execute**, copie o **`id`** do corpo JSON (201), e cole nos passos seguintes onde indicado (`ALUNO_ID`, etc.).  
Se aparecer erro de email/nome repetido (**409** ou regra parecida), mude só o final do email ou o texto do nome (`demo2`, `demo3`).

**Base URL**: `http://localhost:PORT` — Use a porta que aparece no terminal quando sobe o backend (ex.: 8081 com `scripts/run-backend.ps1`).

### Modo rápido (uma execução)

Com o backend **já rodando**, execute no PowerShell (ajuste `-BaseUrl` à porta impressa pelo `run-backend.ps1`):

```powershell
.\scripts\demo-fluxo-api.ps1
.\scripts\demo-fluxo-api.ps1 -BaseUrl 'http://localhost:8081'
```

O script cria turma, disciplinas, aluno, responsável, vínculos, simulado, notas, consulta desempenho e (por predefinição) o fluxo de retificação. Emails e nomes são únicos (sufixo de tempo), pelo que pode correr várias vezes sem conflito de email.

No Swagger, nos **POST**/**PUT** mencionados abaixo, use o dropdown **Examples** no corpo do pedido para exemplos já preenchidos (alguns IDs ainda precisam de ser substituídos pelos valores devolvidos pela API quando a base não está vazia).

---

## Passo 0 — Turma (opcional antes de vincular, mas recomendado)

`POST /turmas`

```json
{
  "nome": "Turma Demo Swagger"
}
```

→ guarde **`TURMA_ID`**.

---

## Passo 1 — Disciplinas (duas)

`POST /disciplinas`

```json
{
  "nome": "Matematica Demo Fluxo"
}
```

→ **`ID_MAT`**  

Repita:

`POST /disciplinas`

```json
{
  "nome": "Portugues Demo Fluxo"
}
```

→ **`ID_PORT`**

---

## Passo 2 — Aluno

`POST /alunos`

```json
{
  "nome": "Aluno Demo Fluxo Swagger",
  "email": "aluno.fluxo.swagger@demo.local"
}
```

→ **`ALUNO_ID`** (se já existia email, altere só o email.)

---

## Passo 3 — Responsável

`POST /responsaveis`

```json
{
  "nome": "Maria Demo Fluxo Swagger",
  "email": "maria.fluxo.swagger@demo.local"
}
```

→ **`RESP_ID`**

---

## Passo 4 — Vincular turma ao aluno (se criou turma)

`PUT /alunos/ALUNO_ID/turma`

Substitua no URL `ALUNO_ID` pelo passo 2.

```json
{
  "turmaId": TURMA_ID
}
```

Troque `TURMA_ID` pelo número retornado no passo 0 (tipo `{"turmaId": 1}` não use texto).

---

## Passo 5 — Vincular responsável ao aluno

`PUT /alunos/ALUNO_ID/responsavel`

```json
{
  "responsavelId": RESP_ID,
  "podeVisualizarNotas": true,
  "podeVisualizarSimulados": true,
  "podeVisualizarDesempenho": true
}
```

Use inteiro (`1`) onde `RESP_ID` corresponda ao **`id`** do passo 3.

---

## Passo 6 — Simulado

`POST /simulados`

```json
{
  "descricao": "Simulado Demo Swagger",
  "disciplinasIds": [ID_MAT, ID_PORT]
}
```

Substitua **`ID_MAT`** e **`ID_PORT`** pelos dois `id` do passo 1 (lista de dois números inteiros, ex.: `[1,2]`).

→ **`SIMULADO_ID`**

---

## Passo 7 — Notas (duas)

`POST /notas`

```json
{
  "alunoId": ALUNO_ID,
  "simuladoId": SIMULADO_ID,
  "disciplinaId": ID_MAT,
  "valor": 6.0
}
```

`POST /notas` (troque disciplina pela outra):

```json
{
  "alunoId": ALUNO_ID,
  "simuladoId": SIMULADO_ID,
  "disciplinaId": ID_PORT,
  "valor": 8.5
}
```

---

## Passo 8 — Desempenho (consulta pelo responsável)

`GET /responsaveis/RESP_ID/alunos/ALUNO_ID/desempenho`

Apenas **Execute** (sem corpo).

---

## Passo 9 — Retificação (exemplo rápido)

Use o **`id`** de uma das notas do passo 7 (**`NOTA_ID`**) como `notaId`.

`POST /retificacoes`

```json
{
  "notaId": NOTA_ID,
  "justificativa": "Demonstracao Swagger - possivel erro de digitacao na planilha"
}
```

→ **`SOLICIT_ID`**, depois:

`PATCH /retificacoes/SOLICIT_ID/em-analise`  

`PATCH /retificacoes/SOLICIT_ID/aprovar`

Corpo exemplo para aprovar:

```json
{
  "novoValorNota": 9.0,
  "justificativaDecisao": "Revisao pedagogica concede ajuste"
}
```

(Substitua **`SOLICIT_ID`** pelo `id` retornado em `POST /retificacoes`.)

---

## Dica

- Os GET **`/notas/ranking`** e **`/notas/ranking/top`** são **legados** e não têm campo de id de aluno; use **`GET /rankings/{simuladoId}`** com o **`SIMULADO_ID`** se quiser ranking por simulado.
- O dropdown **Examples** nos POST (quando aparecer na UI do Swagger) traz outros corpos de exemplo já preenchidos.

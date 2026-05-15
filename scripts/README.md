# Scripts utilitários (`scripts/`)

Scripts PowerShell de apoio ao desenvolvimento e demonstração. Ficam nesta pasta separada do código Java de produção porque são ferramentas de ambiente — não fazem parte do JAR que sobe em produção.

Para executar qualquer script a partir da raiz do projeto:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\<nome-do-script>.ps1
```

---

## `run-backend.ps1` — Subir o backend automaticamente

Sobe o módulo `apresentacao-backend` sem precisar de `mvn` no PATH e sem especificar porta manualmente.

**O que faz:**
1. Localiza o Maven (tenta `MAVEN_HOME`, `M2_HOME`, `tools\apache-maven-*` ou `mvn` no PATH).
2. Varre as portas 8080 a 8299 e escolhe a primeira livre.
3. Passa `-Dserver.port=<porta>` via `spring-boot.run.jvmArguments`.
4. Quando detecta `Started AcadTrackApplication` no output, imprime o URL completo do Swagger.

**Parâmetros:**
- `-Port <número>` — usa uma porta específica em vez de buscar automaticamente.

**Quando usar:** desenvolvimento local e demonstrações quando não tem certeza se a porta 8080 está disponível.

```powershell
# Porta automática
powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1

# Porta fixa
powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1 -Port 9000
```

---

## `demo-fluxo-api.ps1` — Demonstração end-to-end via API

Executa o fluxo completo do sistema contra uma API **já em execução**, criando todos os recursos na ordem correta de dependência:

```
Turma → Disciplinas (2) → Aluno → Responsável
  → Vínculo aluno-turma → Vínculo responsável-aluno (com permissões)
  → Simulado → Notas → Desempenho → Ranking
  → [opcional] Retificação → Análise → Aprovação
```

Usa `Invoke-RestMethod` (PowerShell nativo) para os requests. Gera e-mails únicos por timestamp para evitar conflito de e-mail duplicado entre execuções.

**Parâmetros:**
- `-BaseUrl 'http://localhost:PORTA'` — obrigatório; use a URL que o `run-backend.ps1` imprimiu.
- `-SkipRetificacao` — pula a etapa de retificação de nota.

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\demo-fluxo-api.ps1 -BaseUrl 'http://localhost:8080'
```

**Quando usar:** validar rapidamente que o fluxo completo está funcionando; gerar saída para relatório ou apresentação.

---

## `free-ports-if-needed.ps1` — Liberar portas travadas

Termina processos Java que estejam escutando nas portas **8080** ou **9001** (instâncias antigas do backend que não foram encerradas corretamente). Não afeta outros processos.

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\free-ports-if-needed.ps1
```

**Quando usar:** quando o terminal exibe "port already in use" e você sabe que é uma instância anterior do AcadTrack.

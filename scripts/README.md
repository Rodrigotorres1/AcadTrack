# Scripts na raiz do projeto (`scripts/`)

Estes ficheiros ficam **à parte do código Java de produção** porque são **ferramentas de desenvolvimento e demonstração**, não parte do JAR que sobe em produção. No **README** principal e em `docs/demo_fluxo_swagger_passo_a_passo.md` aponta-se para aqui com caminhos estáveis (`.\scripts\…`).

Usar scripts na **raiz do repositório** é convenção comum: fácil de encontrar, igual em Windows/macOS/Linux, e não depende de instalar ferramentas em pastas ocultas do sistema.

---

## `run-backend.ps1`

**O que é:** comando para **subir o backend Spring Boot** (`módulo apresentacao-backend`) sem precisar de `mvn` no `PATH`.

**O que faz:**

- Resolve o Maven (por exemplo `tools\apache-maven-3.9.9` ou `MAVEN_HOME`).
- Por defeito escolhe a **primeira porta livre** entre 8080 e 8299 (ou porta fixa com `-Port`).
- Passa `-Dserver.port=…` via JVM do Spring Boot (`-Dspring-boot.run.jvmArguments=…`).
- Quando aparece `Started AcadTrackApplication`, imprime o URL do **Swagger** / OpenAPI na consola.

**Quando usar:** desenvolvimento local e demos; é o método recomendado no README quando `mvn` não é encontrado no terminal.

---

## `demo-fluxo-api.ps1`

**O que é:** script de **demonstração end-to-end** contra a API **já a correr** (turma → disciplinas → aluno → responsável → vínculos → simulado → notas → desempenho → retificação opcional).

**O que faz:** envia vários pedidos HTTP (PowerShell `Invoke-RestMethod`) com emails únicos por timestamp e mostra IDs e JSON de resposta.

**Parâmetros úteis:** `-BaseUrl 'http://localhost:PORTA'` (igual à porta que o `run-backend` imprimiu), `-SkipRetificacao` para parar antes da retificação.

**Quando usar:** validar rápido que o fluxo completo está coerente, ou gerar saída de confirmação no terminal (`docs/validacoes.md` refere este script).

---

## `free-ports-if-needed.ps1`

**O que é:** opcional helper para **terminar só processos Java** que estejam à escuta nas portas **8080** ou **9001** (para destravar um backend antigo). Não mata outros programas nem Docker à força.

**Quando usar:** quando o terminal diz porta em uso e sabes que é uma instância antiga Java do projeto.

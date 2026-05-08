# Sistema AcadTrack - Plataforma Acadêmica

**Status:** ✅ Entrega 2 concluída



<p align="center">

<img src="https://img.shields.io/badge/Java-17%2B-007396?logo=openjdk&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven&logoColor=white" />
<img src="https://img.shields.io/badge/Cucumber-BDD-23D96C?logo=cucumber&logoColor=white" />
<img src="https://img.shields.io/badge/JPA%2FHibernate-59666C?logo=hibernate&logoColor=white" />


Sistema backend para gestão acadêmica com foco em simulados, lançamento de notas, análise de desempenho e fluxo de retificação, desenvolvido com regras de negócio não triviais e validações orientadas ao domínio.

O sistema é utilizado para acompanhar o desempenho acadêmico de alunos a partir de simulados, permitindo análise de resultados, identificação de risco e correção de inconsistências por meio de retificação de notas.
Ele apoia decisões acadêmicas com base em fluxos reais de avaliação, acesso responsável e revisão de resultados.

## 🔎 Sumário

- [Links essenciais da entrega](#-links-essenciais-da-entrega)
- [Escopo da Entrega 2](#-escopo-da-entrega-2)
- [Padrões de projeto adotados](#-padrões-de-projeto-adotados-entrega-2)
- [Persistência ORM/JPA](#-persistência-ormpja)
- [Camada de apresentação web](#-camada-de-apresentação-web)
- [Funcionalidades principais](#-funcionalidades-principais-6-oficiais)
- [Arquitetura](#-arquitetura)
- [Personas](#-personas-do-sistema)
- [Como rodar o projeto](#-como-rodar-o-projeto)
- [Testes](#-testes-bdd-com-cucumber)
- [API e Swagger](#-api-e-swagger)
- [Documentação acadêmica](#-documentação-acadêmica)

## 🔗 Links essenciais da entrega

### Artefactos DDD e contexto (revisão de entrega)

- **CML** (bounded contexts e relações no Context Map): [`docs/cml/acadtrack.cml`](docs/cml/acadtrack.cml)
- **Resumo dos bounded contexts:** [`docs/cml/bounded_contexts.md`](docs/cml/bounded_contexts.md)
- **Níveis DDD** (preliminar → operacional): [`docs/ddd_niveis.md`](docs/ddd_niveis.md)

- Documento da entrega 1: [Google Docs — AcadTrack Entrega1](https://docs.google.com/document/d/1cM4kKdVW_yvDcdhpTa-CGE4ilq1bWi1Up5M02WKOrrg/edit?usp=sharing)
- Protótipo: [Abrir Figma](https://stew-skip-70401626.figma.site)
- Slides da apresentação: [Abrir slides no Gamma](https://gamma.app/docs/AcadTrack-fbl5e19j5zy2rvi?mode=doc)
- Story map: [Abrir story map no Avion](https://sistema-acadtrack.avion.io/share/8rNKdtSMQmCNdr3u3)

> Referência oficial de protótipo da entrega: Figma.

### 🎥 Prévia do screencast (imagem clicável)

[![Assistir screencast do AcadTrack](./docs/img/screencast-thumb.png)](https://www.loom.com/share/33626c63524f4091921125d26fdfe3aa)

> Clique na imagem para abrir o screencast completo.


## 📌 Escopo da Entrega 2

Esta entrega mantém todos os requisitos da Entrega 1 e acrescenta:

- **6 padrões de projeto** implementados (1 por integrante): Iterator, Decorator, Observer, Proxy, Strategy e Template Method;
- **Camada de persistência** com mapeamento objeto-relacional via JPA/Hibernate;
- **Camada de apresentação web** integrada (SPA em HTML/CSS/JS servida pelo Spring Boot).

## 📋 Sobre o domínio

O AcadTrack modela o contexto acadêmico envolvendo:

- alunos, responsáveis e turmas;
- disciplinas e simulados;
- notas por aluno/simulado/disciplina;
- análise consolidada de desempenho e risco;
- retificação de nota com fluxo de decisão.

A proposta da Parte 1 é priorizar comportamento de negócio, consistência de regras e rastreabilidade por cenários BDD.

## 🗣️ Linguagem onipresente

Os principais termos do domínio utilizados de forma consistente no sistema são:

- **Aluno**: entidade principal avaliada;
- **Disciplina**: unidade de conhecimento;
- **Simulado**: avaliação composta por disciplinas;
- **Nota**: desempenho do aluno em uma disciplina;
- **Responsável**: usuário com permissão de acesso ao aluno;
- **Retificação**: processo de revisão de nota.
- **Média global simples**: média aritmética de todas as notas do aluno; alimenta média e situação registadas no cadastro do aluno.


## 🎯 Objetivo do sistema

Implementar um sistema acadêmico com funcionalidades de média/alta complexidade, evitando CRUD puro e garantindo:

- fluxos completos por funcionalidade (início, validação, decisão e efeito);
- regras de negócio centralizadas no domínio e casos de uso;
- impacto consistente entre entidades (nota registada → atualização da **média global simples** e da **situação** no aluno; outros fluxos usam média ponderada por simulado — ver secção anterior);
- validação automatizada por cenários Gherkin/Cucumber.

---

## ✨ Funcionalidades principais (6 oficiais)

1. **Gestão de disciplinas com vínculo pedagógico e restrições acadêmicas**
   - controla disponibilidade da disciplina e seu impacto em notas e simulados.
2. **Gestão de responsáveis com controle de acesso**
   - regula vínculo e permissões de consulta por responsável.
3. **Lançamento de notas com cálculo automático da situação acadêmica**
   - registra nota válida e recalcula a **média global simples** e a **situação** do aluno (não confundir com a média ponderada do simulado).
4. **Análise consolidada de desempenho acadêmico com alerta de risco**
   - consolida histórico e classifica risco acadêmico para apoio à decisão.
5. **Criação inteligente de simulados por disciplinas vinculadas**
   - valida composição e consistência do simulado antes da persistência.
6. **Fluxo de retificação de nota com análise e decisão**
   - conduz solicitação, análise e decisão com efeito no desempenho do aluno.

---

## 🧩 Padrões de projeto adotados (Entrega 2)

Seis padrões foram implementados, um por integrante do grupo, aplicados de forma natural ao domínio — sem forçar abstrações desnecessárias.

| Padrão | Onde é aplicado | Integrante |
|---|---|---|
| **Template Method** | `FluxoAnaliseAcademicaTemplate` — define o esqueleto do fluxo de análise acadêmica (coleta → consolidação → classificação → notificação); subclasses especializam etapas | Rodrigo Torres |
| **Decorator** | `ValidadorValorNota`, `ValidadorNotaDuplicada`, `ValidadorEntidadesLancamento`, `ValidadorDisciplinaAtiva`, `ValidadorDisciplinaVinculadaSimulado` — cadeia de validações composta dinamicamente no lançamento de notas | Erick Belo |
| **Observer** | `EventoNotaLancada` / listeners de situação e notificação — aluno e responsável são notificados após mudança de nota ou situação acadêmica | Erick Belo |
| **Iterator** | Ranking acadêmico — percorre a coleção ordenada de alunos por média/risco sem expor a estrutura interna | João Marcelo Montenegro |
| **Proxy** | `ResponsavelAcessoProxy` — intermedia a consulta do responsável aos dados do aluno, verificando permissão antes de delegar ao repositório real | João Marcelo Montenegro |
| **Strategy** | `ClassificadorRiscoAcademico` — permite trocar o critério de classificação de risco (por média, por frequência, combinado) sem alterar o fluxo de análise | Rodrigo Torres |

Documentos de apoio:

- Padrões de projeto: [`docs/padroes_entrega2.md`](docs/padroes_entrega2.md)
- Checklist técnico: [`docs/checklist_entrega2.md`](docs/checklist_entrega2.md)

---

## 🗄️ Persistência ORM/JPA

A camada de persistência utiliza **Spring Data JPA com Hibernate** e banco **H2** (arquivo em disco na pasta `data/`).

Principais decisões de mapeamento:

- Entidades anotadas com `@Entity`, `@Table`, `@Id`, `@GeneratedValue` nos módulos de domínio;
- Relacionamentos mapeados com `@ManyToOne`, `@OneToMany` e `@JoinColumn` onde aplicável;
- Repositórios implementados como interfaces `JpaRepository` no módulo `infraestrutura/`;
- Inicialização de dados de demonstração via `DadosIniciaisConfig` (`@Component` com `CommandLineRunner`);
- Schema gerado automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

Referência de configuração: [`apresentacao-backend/src/main/resources/application.properties`](apresentacao-backend/src/main/resources/application.properties)

Documentação detalhada: [`docs/persistencia_orm_entrega2.md`](docs/persistencia_orm_entrega2.md)

---

## 🌍 Camada de apresentação web

O frontend é uma **SPA em HTML/CSS/JavaScript puro** servida diretamente pelo Spring Boot a partir de:

```text
apresentacao-backend/src/main/resources/static/
├── index.html
├── styles.css
└── app.js
```

Funcionalidades da interface:

- Cadastro e listagem de alunos, disciplinas, simulados e turmas;
- Lançamento de notas e visualização de histórico por aluno;
- Cálculo e exibição de média ponderada por simulado;
- Ranking acadêmico com classificação de risco;
- Fluxo de retificação de nota (solicitação → análise → decisão);
- Gestão de responsáveis e vínculos com alunos.

O módulo `apresentacao-frontend/` é um placeholder Maven (`packaging=pom`) que representa conceitualmente a camada web; os arquivos de interface residem em `apresentacao-backend/static/` e são servidos pelo Tomcat embutido.

---

---

## 🧠 Critério de complexidade (defesa da entrega)

As funcionalidades da Parte 1 são tratadas como **fluxos completos de negócio**, e não como ações isoladas.

Cada fluxo contempla:

- entrada de dados pelo usuário;
- validações de regras de negócio;
- persistência e consulta em banco via repositórios;
- resultado funcional observável para o usuário.

Em outras palavras, itens como "calcular média" ou "criar aluno" não são apresentados de forma isolada, mas como partes de jornadas maiores com começo, meio e fim.

### Fluxos funcionais consolidados

**Fluxo de avaliação acadêmica**

- lançar nota;
- buscar notas;
- calcular média;
- analisar desempenho;
- gerar ranking.

**Fluxo de retificação de nota**

- solicitar retificação;
- iniciar análise;
- aprovar;
- reprovar.

**Fluxo de responsáveis**

- criar responsável;
- vincular ao aluno;
- validar acesso;
- consultar notas/desempenho/simulados;
- desvincular.

**Fluxo de simulados**

- criar simulado;
- vincular disciplinas;
- validar composição.

**Fluxo acadêmico básico**

- criar aluno;
- criar turma;
- vincular aluno à turma;
- criar/ativar/inativar disciplina.


## 🏗️ Arquitetura

O projeto segue **Clean Architecture**, com separação de responsabilidades por camadas:

- **Domínio**: entidades, invariantes e regras de negócio;
- **Aplicação**: casos de uso e orquestração de fluxos;
- **Infraestrutura**: persistência e implementações de repositórios;
- **Apresentação**: API REST, DTOs, validações e tratamento de erros.

Essa organização permite evoluir regras sem acoplamento indevido com framework ou camada de transporte.

---

## 👤 Personas do sistema

- **Coordenador**: gerencia disciplinas, simulados, turmas e vínculos.
- **Professor**: lança notas e participa do fluxo de avaliação/retificação.
- **Aluno**: acompanha notas, desempenho e solicita retificação.
- **Responsável**: consulta dados do aluno vinculado conforme permissões.

Observação importante sobre a entrega atual:

- O **Coordenador** está modelado como **persona de negócio** (documentação e cenários BDD), mas **ainda não está implementado tecnicamente como role/perfil de autenticação** no backend.
- As ações do coordenador são expostas por controllers acadêmicos (por exemplo, disciplinas, simulados e turmas), sem controle de acesso por perfil nesta versão.

---

## 📦 Estrutura do projeto

```text
AcadTrack/
├── .mvn/                    (Maven Wrapper)
├── aplicacao/
├── apresentacao-backend/
├── apresentacao-frontend/   (placeholder Maven — sem artefactos; interface servida por apresentacao-backend/static/)
├── bdd/
│   └── acadtrackbdd/
├── docs/
│   ├── cml/
│   └── validacoes/          (capturas PNG)
├── dominio-academico/
├── dominio-avaliacao/
├── dominio-usuarios/
├── dominio-compartilhado/
├── infraestrutura/
├── scripts/
├── mvnw, mvnw.cmd
└── pom.xml
```

---

## 🚀 Como rodar o projeto

### Pré-requisitos

- **Java (JDK)** — mínimo **17** (definido pelo Spring Boot / `pom.xml`). 

- **Nota sobre versões:** o código foi **validado com sucesso** (`mvnw clean verify`, subida do backend) num ambiente de desenvolvimento com **JDK 25**. 

- **Maven**, de uma destas formas na **raiz do repo** (`pom.xml`):

| Situação | Como invocar |
|----------|----------------|
| `mvn` no `PATH` | `mvn …` |
| **Windows PowerShell** (Maven não no PATH ou comando não encontrado) | **`.\mvnw.cmd`** (obrigatório o prefixo **`.\`** — apenas `mvnw` falha por política do PowerShell) |
| Git Bash em Windows | `./mvnw` |
| Linux / macOS | `./mvnw` |

### Comandos rápidos (wrapper no Windows — recomendado)

**Passo a passo para rodar pelo terminal**

1. Abra o PowerShell na raiz do projeto, onde fica o arquivo `pom.xml`.

2. Execute o backend Spring Boot junto com os módulos dependentes:

```powershell
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run
```

3. Acesse a interface web no navegador:

```text
http://localhost:8080/
```

4. Acesse o Swagger, se quiser testar a API diretamente:

```text
http://localhost:8080/swagger-ui/index.html
```

5. Se a porta `8080` estiver ocupada, rode em outra porta:

```powershell
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Nesse caso, acesse:

```text
http://localhost:8081/
```

**Observação sobre a camada web**

Os arquivos da interface (HTML, CSS, JavaScript) residem em `apresentacao-backend/src/main/resources/static/` e são servidos pelo Tomcat embutido. Detalhes na seção [Camada de apresentação web](#-camada-de-apresentação-web).

**Build completo**

```powershell
.\mvnw.cmd clean install
```

**Subir backend** na porta definida por defeito (**8080** em `application.properties`):

```powershell
.\mvnw.cmd -pl apresentacao-backend -am spring-boot:run
```

### Script `run-backend.ps1` (porta livre automática)

O ficheiro [`scripts/run-backend.ps1`](scripts/run-backend.ps1) **usa o Maven instalado**

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-backend.ps1
```

Este script usa a **primeira porta livre** entre **8080–8299** e imprime o URL real do Swagger.

### IDE ou Maven sem terminal

Ou **IDE**: abrir `AcadTrackApplication.java` (`apresentacao-backend`) → **Run/Debug** (`main`), normalmente porta **8080**.

### Libertar porta (Java em 8080 / 9001)

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\free-ports-if-needed.ps1
```

Detalhes: [`scripts/README.md`](scripts/README.md).

### Outra porta (linha de comando, estilo igual ao script no Windows)

```powershell
.\mvnw.cmd spring-boot:run -pl apresentacao-backend "-Dspring-boot.run.jvmArguments=-Dserver.port=9001"
```

### Rodar testes

**Todos os módulos**:

```powershell
.\mvnw.cmd test
```

**Só BDD** (útil no dia a dia):

```powershell
.\mvnw.cmd -pl bdd/acadtrackbdd test
```


### Depois de subir

Abre o Swagger na secção **API e Swagger** abaixo. Se usaste **`run-backend.ps1`**, a porta pode **não** ser 8080 — usa sempre o URL que o script imprime.

---

## 🧪 Testes (BDD com Cucumber)

Os testes de comportamento estão centralizados em `bdd/acadtrackbdd` com cenários Gherkin.

- Suite BDD automatizada com cenários principais e de apoio;
- Cobertura de fluxos positivos e negativos;
- Validação de regras de negócio e contratos comportamentais do sistema.

Execução dos testes (equivalentes à secção **Como rodar**): na raiz, `.\mvnw.cmd test` (Windows) ou `./mvnw test`; só Cucumber:

```powershell
.\mvnw.cmd -pl bdd/acadtrackbdd test
```

### Organização dos arquivos `.feature`

Funcionalidades principais (oficiais da entrega):

- `gestao_disciplina.feature`
- `vincular_responsavel.feature`
- `lancar_nota.feature`
- `analise_desempenho.feature`
- `criar_simulado.feature`
- `solicitar_retificacao_nota.feature`

Cenários complementares/legados de apoio ao domínio (mantidos no projeto):

- `extra/calcular_media_ponderada.feature`
- `extra/gerar_ranking.feature`
- `extra/vincular_aluno_turma.feature`

---

## ✅ Estado atual da entrega

- Cenários BDD passando via `.\mvnw.cmd test`.
- Funcionalidades principais organizadas em fluxos completos.

---

## 🌐 API e Swagger

Com o backend em execução, a documentação interativa da API pode ser acessada em:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI (JSON): `http://localhost:8080/v3/api-docs`

Observações para demonstração e testes:

- É necessário iniciar o backend antes de abrir o Swagger (a porta pode ser outra se usar `scripts/run-backend.ps1`, que escolhe uma livre e imprime a URL).

- **Fluxo automático:** com o servidor já rodando, `.\scripts\demo-fluxo-api.ps1 -BaseUrl 'http://localhost:PORT'` (ajuste `PORT`; ver [`docs/demo_fluxo_swagger_passo_a_passo.md`](docs/demo_fluxo_swagger_passo_a_passo.md)).
- Execute os fluxos na **ordem do roteiro** em [`docs/script_demonstracao.md`](docs/script_demonstracao.md) ou no guia Swagger acima: muitos endpoints dependem de `id`s criados antes (turma → disciplinas → aluno → responsável → simulado → nota → retificação).
- Nos **POST de criação**, use apenas os campos do **request body** descritos no Swagger; em geral o `id` é gerado pelo sistema (valores opcionais no JSON costumam ser ignorados).
- Como saber se deu certo: em criação espere **201** e um JSON com o recurso criado (incluindo `id`); erros esperados costumam ser **400** (validação/regra), **404** (não encontrado) ou **409** (conflito). Detalhes em [`docs/script_demonstracao.md`](docs/script_demonstracao.md).

Validações (prints opcionais): [`docs/validacoes.md`](docs/validacoes.md) (capturas em `docs/validacoes/`).

---

## 📚 Documentação acadêmica

Os artefatos da Parte 1 (story map, protótipos, CML, descrição de domínio e materiais de apoio) estão organizados na pasta de documentação do projeto:

- `docs/` (equivalente ao pacote de docs da entrega)
- `docs/cml/acadtrack.cml` (modelo CML consolidado)
- `docs/story_map_personas.md` (story map com personas e releases)
- `docs/story_map.pdf` (story map visual em PDF)

---

## ⭐ Diferenciais do projeto

- Regras de negócio não triviais implementadas de forma incremental;
- Fluxos completos por funcionalidade (não apenas validações isoladas);
- Integração entre domínio, casos de uso, API e BDD;
- Cenários automatizados como documentação viva da solução.

Este projeto representa a base da evolução do sistema AcadTrack, sendo estendido nas próximas entregas com novas funcionalidades e camadas de interação.

---

## 👥 Membros

- **Erick Belo** - eab2@cesar.school  
  Funcionalidades foco: F3 - Lançamento de notas com cálculo automático da situação acadêmica; F6 - Fluxo de retificação de nota com análise e decisão.
- **João Marcelo Montenegro** - jmtpm@cesar.school  
  Funcionalidades foco: F1 - Gestão de disciplinas com vínculo pedagógico e restrições acadêmicas; F2- Gestão de responsáveis com controle de acesso.
- **Rodrigo Torres** - rtgf@cesar.school  
  Funcionalidades foco: F4 - Análise consolidada de desempenho acadêmico com alerta de risco; F5 - Criação inteligente de simulados por disciplinas vinculadas.

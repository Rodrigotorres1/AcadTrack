# Sistema AcadTrack - Plataforma Acadêmica

**Status:** ✅ Entrega 1 concluída

| |
|:--:|
| ![Java 17](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white) ![Spring Boot 3.x](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white) ![Maven 3.8+](https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven&logoColor=white) ![Cucumber BDD](https://img.shields.io/badge/Cucumber-BDD-23D96C?logo=cucumber&logoColor=white) ![JPA Hibernate](https://img.shields.io/badge/JPA%2FHibernate-59666C?logo=hibernate&logoColor=white) |
| Sistema backend de gestão acadêmica com foco em simulados, avaliação de desempenho e fluxo de retificação de notas |
| 🚀 [Começar](#como-rodar-o-projeto) • 📚 [Documentação](#documentação-acadêmica) • 🏗️ [Arquitetura](#arquitetura) • 🧪 [Testes](#testes-bdd-com-cucumber) |

Sistema backend para gestão acadêmica com foco em simulados, lançamento de notas, análise de desempenho e fluxo de retificação, desenvolvido com regras de negócio não triviais e validações orientadas ao domínio.

O sistema é utilizado para acompanhar o desempenho acadêmico de alunos a partir de simulados, permitindo análise de resultados, identificação de risco e correção de inconsistências por meio de retificação de notas.
Ele apoia decisões acadêmicas com base em fluxos reais de avaliação, acesso responsável e revisão de resultados.

## 🔎 Sumário rápido

- [Escopo da Entrega 1](#-escopo-da-entrega-1)
- [Funcionalidades principais](#-funcionalidades-principais-6-oficiais)
- [Arquitetura](#-arquitetura)
- [Como rodar o projeto](#-como-rodar-o-projeto)
- [Testes](#-testes-bdd-com-cucumber)
- [API e Swagger](#-api-e-swagger)
- [Documentação acadêmica](#-documentação-acadêmica)

## 📌 Escopo da Entrega 1

Este repositório representa a **Entrega 1** do projeto AcadTrack, com foco em:

- modelagem de domínio e aplicação de DDD;
- funcionalidades de média/alta complexidade organizadas em fluxos completos;
- cenários BDD automatizados como evidência de comportamento.

O objetivo desta etapa é comprovar coerência entre domínio, regras de negócio, API e testes, mantendo arquitetura limpa e rastreabilidade acadêmica.

## 📋 Sobre o domínio

O AcadTrack modela o contexto acadêmico envolvendo:

- alunos, responsáveis, professores e turmas;
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

---

## 🎯 Objetivo do sistema

Implementar um sistema acadêmico com funcionalidades de média/alta complexidade, evitando CRUD puro e garantindo:

- fluxos completos por funcionalidade (início, validação, decisão e efeito);
- regras de negócio centralizadas no domínio e casos de uso;
- impacto consistente entre entidades (ex.: nota -> média -> situação acadêmica);
- validação automatizada por cenários Gherkin/Cucumber.

O sistema não se limita a operações CRUD, sendo baseado em fluxos completos com regras de negócio e decisões que impactam diretamente o estado do sistema.

---

## ✨ Funcionalidades principais (6 oficiais)

1. **Gestão de disciplinas com vínculo pedagógico e restrições acadêmicas**
   - controla disponibilidade da disciplina e seu impacto em notas e simulados.
2. **Gestão de responsáveis com controle de acesso**
   - regula vínculo e permissões de consulta por responsável.
3. **Lançamento de notas com cálculo automático da situação acadêmica**
   - registra nota válida e recalcula média/situação do aluno automaticamente.
4. **Análise consolidada de desempenho acadêmico com alerta de risco**
   - consolida histórico e classifica risco acadêmico para apoio à decisão.
5. **Criação inteligente de simulados por disciplinas vinculadas**
   - valida composição e consistência do simulado antes da persistência.
6. **Fluxo de retificação de nota com análise e decisão**
   - conduz solicitação, análise e decisão com efeito no desempenho do aluno.

---

## 🧠 Critério de complexidade (defesa da entrega)

As funcionalidades da Parte 1 são tratadas como **fluxos completos de negócio**, e não como ações isoladas.

Cada fluxo contempla:

- entrada de dados pelo usuário;
- validações de regras de negócio;
- persistência e consulta em banco via repositórios;
- resultado funcional observável para o usuário.

Em outras palavras, itens como "calcular média", "gerar ranking" ou "criar aluno" não são apresentados de forma isolada, mas como partes de jornadas maiores com começo, meio e fim.

### Fluxos funcionais consolidados

**Fluxo de avaliação acadêmica**

- lançar nota;
- buscar notas;
- calcular média;
- calcular média ponderada;
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
- criar professor;
- criar turma;
- vincular aluno à turma;
- criar/ativar/inativar disciplina.

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Papel no projeto |
|---|---|
| Java 17+ | Linguagem principal da aplicação |
| Spring Boot | Framework para API e configuração da aplicação |
| JPA (Hibernate) | Mapeamento objeto-relacional e persistência |
| Maven | Build e gerenciamento de dependências |
| Cucumber (BDD) | Testes de comportamento com cenários Gherkin |

---

## 🏗️ Arquitetura

O projeto segue **Clean Architecture**, com separação de responsabilidades por camadas:

- **Domínio**: entidades, invariantes e regras de negócio;
- **Aplicação**: casos de uso e orquestração de fluxos;
- **Infraestrutura**: persistência e implementações de repositórios;
- **Apresentação**: API REST, DTOs, validações e tratamento de erros.

Essa organização permite evoluir regras sem acoplamento indevido com framework ou camada de transporte.

---

## 📦 Estrutura do projeto

```text
AcadTrack/
├── dominio-academico/
├── dominio-avaliacao/
├── dominio-usuarios/
├── dominio-compartilhado/
├── aplicacao/
├── infraestrutura/
├── apresentacao-backend/
├── bdd/
│   └── acadtrackbdd/
├── Documentacao/
└── pom.xml
```

---

## 🚀 Como rodar o projeto

### Pré-requisitos

- Java 17+
- Maven

### Comandos rápidos

**Build completo**
```bash
mvn clean install
```

**Subir backend**
```bash
mvn spring-boot:run -pl apresentacao-backend -Dspring-boot.run.arguments=--server.port=9001
```

**Rodar testes**
```bash
mvn test
```

Após subir o backend, acesse o Swagger para testar os endpoints:
Veja a seção **API e Swagger** para o link e observações de acesso.

---

## 🧪 Testes (BDD com Cucumber)

Os testes de comportamento estão centralizados em `bdd/acadtrackbdd` com cenários Gherkin.

- Total atual: **~39 cenários** automatizados;
- Cobertura de fluxos positivos e negativos;
- Validação de regras de negócio e contratos comportamentais do sistema.

Execução dos testes:

```bash
mvn test
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
- `extra/definir_peso_disciplina.feature`
- `extra/gerar_ranking.feature`
- `extra/vincular_aluno_turma.feature`

---

## ✅ Estado atual da entrega

- 39 cenários BDD passando.
- Funcionalidades principais organizadas em fluxos completos.

---

## 🌐 API e Swagger

Com o backend em execução, a documentação interativa da API pode ser acessada em:

- `http://localhost:9001/swagger-ui/index.html`

Observação: é necessário iniciar o backend antes de abrir o Swagger.

---

## 📚 Documentação acadêmica

Os artefatos da Parte 1 (story map, protótipos, CML, descrição de domínio e materiais de apoio) estão organizados na pasta de documentação do projeto:

- `Documentacao/` (equivalente ao pacote de docs da entrega)

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
  Funcionalidades foco: Gestão de disciplinas; Criação inteligente de simulados.
- **João Marcelo Montenegro** - jmtpm@cesar.school  
  Funcionalidades foco: Lançamento de notas; Análise consolidada de desempenho.
- **Rodrigo Torres** - rtgf@cesar.school  
  Funcionalidades foco: Gestão de responsáveis; Fluxo de retificação de nota.
# AcadTrack - Plataforma Acadêmica (Parte 1)

Sistema backend para gestão acadêmica com foco em simulados, lançamento de notas, análise de desempenho e fluxo de retificação, desenvolvido com regras de negócio não triviais e validações orientadas ao domínio.

## 👥 Membros

- João Marcelo Montenegro
- Rodrigo Torres
- Erick Belo
- Victor Vilela

## 📋 Sobre o domínio

O AcadTrack modela o contexto acadêmico envolvendo:

- alunos, responsáveis, professores e turmas;
- disciplinas e simulados;
- notas por aluno/simulado/disciplina;
- análise consolidada de desempenho e risco;
- retificação de nota com fluxo de decisão.

A proposta da Parte 1 é priorizar comportamento de negócio, consistência de regras e rastreabilidade por cenários BDD.

---

## 🎯 Objetivo do sistema

Implementar um sistema acadêmico com funcionalidades de média/alta complexidade, evitando CRUD puro e garantindo:

- fluxos completos por funcionalidade (início, validação, decisão e efeito);
- regras de negócio centralizadas no domínio e casos de uso;
- impacto consistente entre entidades (ex.: nota -> média -> situação acadêmica);
- validação automatizada por cenários Gherkin/Cucumber.

---

## ✨ Funcionalidades principais (6 oficiais)

1. **Gestão de disciplinas com vínculo pedagógico e restrições acadêmicas**
2. **Gestão de responsáveis com controle de acesso**
3. **Lançamento de notas com cálculo automático da situação acadêmica**
4. **Análise consolidada de desempenho acadêmico com alerta de risco**
5. **Criação inteligente de simulados por disciplinas vinculadas**
6. **Fluxo de retificação de nota com análise e decisão**

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

- **Java 17+**
- **Spring Boot**
- **JPA (Hibernate)**
- **Maven**
- **Cucumber (BDD)**

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

### Build completo

```bash
mvn clean install
```

### Subir backend

```bash
mvn spring-boot:run -pl apresentacao-backend -Dspring-boot.run.arguments=--server.port=9001
```

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

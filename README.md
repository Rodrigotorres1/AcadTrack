# AcadTrack

## Sobre o projeto
O AcadTrack é um sistema acadêmico desenvolvido para gerenciamento de simulados, lançamento de notas e acompanhamento do desempenho de alunos.

O sistema permite calcular médias ponderadas com base em pesos por disciplina, gerar rankings e apoiar a tomada de decisão acadêmica.

---

## Equipe
- João Marcelo Montenegro
- (adicione os outros integrantes aqui)

---

## Documentação da 1ª entrega

- Descrição do domínio → Documentacao/descricao-do-dominio.md  
- Mapa de histórias → Documentacao/mapa-de-historias.pdf  
- Protótipos → Documentacao/prototipos.md  
- DDD níveis → Documentacao/ddd-niveis.md  
- Arquitetura limpa → Documentacao/arquitetura-limpa.md  
- BDD → Documentacao/bdd.md  
- Evidências de execução → Documentacao/evidencias.md  

---

## Funcionalidades implementadas (Release 1)

- Cadastrar aluno
- Cadastrar simulado
- Cadastrar disciplina
- Vincular disciplina ao simulado com peso
- Lançar nota para aluno em disciplina
- Calcular média ponderada por simulado
- Gerar ranking de alunos
- Vincular responsável ao aluno

Todas as funcionalidades possuem regras de negócio aplicadas, não sendo apenas operações CRUD.

---

## Tecnologias utilizadas

- Java
- Spring Boot
- JPA (Hibernate)
- Banco de dados H2
- Maven
- Cucumber (BDD)

---

## Arquitetura

O sistema foi desenvolvido seguindo os princípios de **Arquitetura Limpa**, dividido em camadas:

- **Domínio** → regras de negócio  
- **Aplicação** → casos de uso  
- **Infraestrutura** → persistência de dados  
- **Apresentação** → controllers REST  
- **BDD** → testes automatizados  

---

## Como executar o projeto

### Pré-requisitos
- Java 17+
- Maven

### Passos

```bash
mvn clean install
mvn spring-boot:run -pl apresentacao-backend

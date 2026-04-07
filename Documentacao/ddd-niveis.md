# DDD - Níveis

## Nível preliminar

No nível preliminar, foi identificado o problema central do domínio: gerenciar simulados acadêmicos e acompanhar o desempenho dos alunos de forma estruturada, permitindo o lançamento de notas, o cálculo de médias ponderadas, a geração de ranking e o tratamento de solicitações de retificação.

Também foram identificados os principais conceitos do domínio, como aluno, professor, responsável, turma, simulado, disciplina, nota, média ponderada, ranking e solicitação de retificação.

---

## Nível estratégico

No nível estratégico, o sistema foi dividido em subdomínios com responsabilidades bem definidas.

### Gestão Acadêmica (Core Domain)
É o domínio principal do sistema, concentrando as regras de negócio de maior valor. É responsável pela criação e gerenciamento de simulados, definição de disciplinas e pesos, cálculo de médias ponderadas, geração de ranking e acompanhamento do desempenho acadêmico.

### Avaliação (Domínio de Suporte)
É responsável pelo processamento das informações avaliativas do sistema, incluindo lançamento de notas, cálculo de médias ponderadas, ordenação dos alunos no ranking e registro de solicitações de retificação.

### Usuários (Domínio Genérico)
É responsável pelo gerenciamento dos perfis do sistema, como aluno, professor, coordenador e responsável, além de regras relacionadas a autenticação, autorização e controle de acesso.

Esses subdomínios foram organizados e representados no projeto por meio da separação em módulos e pelo modelo construído com Context Mapper.

---

## Nível tático

No nível tático, foi realizada a modelagem do domínio com foco em entidades, repositórios, serviços e casos de uso.

### Entidades
As principais entidades modeladas no sistema são:

- Aluno
- Turma
- Simulado
- Disciplina
- Nota
- Professor
- Responsável
- Solicitação de Retificação

### Objetos de valor
Os principais objetos de valor identificados no sistema são:

- Email
- Peso da disciplina
- Média ponderada

### Agregados
Os agregados foram organizados em torno das entidades principais do domínio, como:

- Aluno
- Simulado
- Nota
- Solicitação de Retificação

### Serviços e casos de uso
As principais regras de negócio foram implementadas por meio de serviços e casos de uso, tais como:

- Lançar nota
- Calcular média ponderada
- Gerar ranking
- Solicitar retificação de nota
- Vincular aluno à turma
- Vincular e desvincular responsável
- Definir peso de disciplina no simulado
- Criar simulados com disciplinas vinculadas

### Repositórios
Foram definidas interfaces de repositório para abstrair a persistência das entidades e agregados do domínio, mantendo a separação entre regras de negócio e infraestrutura.

---

## Nível operacional

No nível operacional, o sistema foi implementado com foco na execução prática da arquitetura e das regras de negócio definidas nos níveis anteriores.

- Implementação backend em Java com Spring Boot
- Persistência em banco de dados relacional com JPA
- Exposição das funcionalidades por meio de API web
- Separação em camadas seguindo arquitetura limpa
- Organização em módulos de domínio, aplicação, infraestrutura e apresentação
- Automatização dos cenários BDD com Cucumber
- Validação das funcionalidades por meio de testes de comportamento e testes dos fluxos principais do sistema

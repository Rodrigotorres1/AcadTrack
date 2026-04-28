# DDD - Niveis

## Nivel preliminar

No nivel preliminar, foi identificado o problema central do dominio: gerenciar simulados academicos e acompanhar o desempenho dos alunos de forma estruturada, permitindo o lancamento de notas, o calculo de medias ponderadas, a geracao de ranking e o tratamento de solicitacoes de retificacao.

Tambem foram identificados os principais conceitos do dominio, como aluno, professor, responsavel, turma, simulado, disciplina, nota, media ponderada, ranking e solicitacao de retificacao.

---

## Nivel estrategico

No nivel estrategico, o sistema foi dividido em subdominios com responsabilidades bem definidas.

### Gestao Academica (Core Domain)
E o dominio principal do sistema, concentrando as regras de negocio de maior valor. E responsavel pela criacao e gerenciamento de simulados, definicao de disciplinas e pesos, calculo de medias ponderadas, geracao de ranking e acompanhamento do desempenho academico.

### Avaliacao (Dominio de Suporte)
E responsavel pelo processamento das informacoes avaliativas do sistema, incluindo lancamento de notas, calculo de medias ponderadas, ordenacao dos alunos no ranking e registro de solicitacoes de retificacao.

### Usuarios (Dominio Generico)
E responsavel pelo gerenciamento dos perfis do sistema, como aluno, professor, coordenador e responsavel, alem de regras relacionadas a autenticacao, autorizacao e controle de acesso.

Observacao de escopo da entrega atual:
- O papel de **coordenador** esta modelado como persona de negocio nos fluxos e na documentacao.
- Ainda nao ha role/perfil tecnico de autenticacao "coordenador" implementado no backend.

Esses subdominios foram organizados e representados no projeto por meio da separacao em modulos e pelo modelo construido com Context Mapper.

---

## Nivel tatico

No nivel tatico, foi realizada a modelagem do dominio com foco em entidades, repositorios, servicos e casos de uso.

### Entidades
As principais entidades modeladas no sistema sao:

- Aluno
- Turma
- Simulado
- Disciplina
- Nota
- Professor
- Responsavel
- Solicitacao de Retificacao

### Objetos de valor
Os principais objetos de valor identificados no sistema sao:

- Email
- Peso da disciplina
- Media ponderada

### Agregados
Os agregados foram organizados em torno das entidades principais do dominio, como:

- Aluno
- Simulado
- Nota
- Solicitacao de Retificacao

### Servicos e casos de uso
As principais regras de negocio foram implementadas por meio de servicos e casos de uso, tais como:

- Lancar nota
- Calcular media ponderada
- Gerar ranking
- Solicitar retificacao de nota
- Vincular aluno a turma
- Vincular e desvincular responsavel
- Definir peso de disciplina no simulado
- Criar simulados com disciplinas vinculadas

### Repositorios
Foram definidas interfaces de repositorio para abstrair a persistencia das entidades e agregados do dominio, mantendo a separacao entre regras de negocio e infraestrutura.

---

## Nivel operacional

No nivel operacional, o sistema foi implementado com foco na execucao pratica da arquitetura e das regras de negocio definidas nos niveis anteriores.

- Implementacao backend em Java com Spring Boot
- Persistencia em banco de dados relacional com JPA
- Exposicao das funcionalidades por meio de API web
- Separacao em camadas seguindo arquitetura limpa
- Organizacao em modulos de dominio, aplicacao, infraestrutura e apresentacao
- Automatizacao dos cenarios BDD com Cucumber
- Validacao das funcionalidades por meio de testes de comportamento e testes dos fluxos principais do sistema
- Implementacao de controle de acesso por perfil (incluindo coordenador tecnico) permanece como evolucao planejada

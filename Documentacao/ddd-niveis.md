# DDD - Níveis

## Nível preliminar
Identificação do problema: gerenciar simulados acadêmicos e acompanhar o desempenho dos alunos, incluindo cálculo de médias e geração de ranking.

## Nível estratégico
Subdomínios:

- Gestão Acadêmica (Core Domain)
  Responsável pelas principais regras de negócio, como criação de simulados, definição de disciplinas e pesos, cálculo de médias e geração de ranking.

- Avaliação (Domínio de Suporte)
  Responsável pelo processamento das notas, cálculo de médias ponderadas e classificação dos alunos.

- Usuários (Domínio Genérico)
  Responsável pelo gerenciamento de usuários, autenticação, autorização e controle de acesso.


## Nível tático
Modelagem do domínio com:
- Entidades: Aluno, Turma, Simulado, Nota, Professor, Responsável
- Objetos de valor: PesoDisciplina, MediaPonderada, Email
- Agregados: Aluno, Simulado, Nota, RetificacaoNota
- Serviços: Geração de ranking, cálculo de média ponderada, lançamento de notas
- Repositórios: interfaces para persistência dos agregados


## Nível tático
Modelagem do domínio com:
- Entidades: Aluno, Turma, Simulado, Nota, Professor, Responsável
- Objetos de valor: PesoDisciplina, MediaPonderada, Email
- Agregados: Aluno, Simulado, Nota, RetificacaoNota
- Serviços: Geração de ranking, cálculo de média ponderada, lançamento de notas
- Repositórios: interfaces para persistência dos agregados

### Entidades:
- Aluno
- Simulado
- Disciplina
- Nota
- Turma
- Professor

### Serviços:
- Geração de ranking
- Cálculo de média ponderada
- Processamento de notas

## Nível operacional
- Implementação das regras de negócio no backend utilizando Spring Boot
- Persistência em banco de dados relacional com JPA
- Interface web para interação dos usuários
- Cenários BDD automatizados com Cucumber para validação do comportamento do sistema

# Descrição do domínio

O AcadTrack é um sistema web voltado para o gerenciamento de simulados acadêmicos e acompanhamento do desempenho de alunos.

O sistema foi projetado para apoiar coordenadores e professores na organização de avaliações e análise de resultados, além de permitir que alunos acompanhem seu desempenho.

---

# Domínios do sistema

## Domínio Principal (Core Domain)
Gestão Acadêmica

Responsável pelas principais regras de negócio do sistema:
- criação e gerenciamento de simulados
- definição de disciplinas e pesos
- cálculo de médias
- geração de ranking de alunos

Esse é o núcleo do sistema, onde está a lógica mais importante.

---

## Domínio de Suporte
Avaliação

Responsável por:
- processamento de notas
- cálculo de médias ponderadas
- ordenação e classificação de alunos

---

## Domínio Genérico
Usuários

Responsável por:
- gerenciamento de usuários
- definição de perfis (coordenador, professor, aluno)
- controle de acesso ao sistema

---

# Atores do sistema

## Coordenador
- cadastrar simulados
- definir disciplinas e pesos
- gerenciar turmas
- visualizar ranking geral
- encerrar simulados

## Professor
- lançar notas dos alunos
- corrigir notas
- acompanhar desempenho das turmas

## Aluno
- visualizar suas notas
- acompanhar sua posição no ranking
- analisar seu desempenho

---

# Jornada do usuário

O fluxo principal do sistema ocorre da seguinte forma:

1. O coordenador cria um simulado e define suas disciplinas e pesos
2. Os alunos são organizados em turmas
3. O professor lança as notas dos alunos para cada disciplina
4. O sistema calcula automaticamente a média final ponderada
5. O sistema gera o ranking dos alunos com base no desempenho
6. O aluno acessa o sistema para visualizar seus resultados e posição

---

# Linguagem onipresente

- Aluno: participante que realiza o simulado
- Professor: responsável por lançar e acompanhar notas
- Coordenador: responsável pela gestão acadêmica
- Simulado: avaliação organizada no sistema
- Disciplina: componente avaliado
- Nota: resultado obtido pelo aluno
- Ranking: classificação dos alunos com base no desempenho
- Média ponderada: cálculo da média considerando pesos das disciplinas

---

# Regras de negócio principais

- Um simulado deve possuir pelo menos uma disciplina
- Cada disciplina pode possuir peso
- A média final deve considerar os pesos definidos
- O ranking deve ordenar os alunos do maior para o menor desempenho
- Um simulado encerrado não pode ser alterado

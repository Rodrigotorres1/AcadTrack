# Descrição do domínio

O AcadTrack é um sistema web para gerenciamento de simulados acadêmicos.

O sistema permite cadastrar simulados, associar disciplinas, registrar notas dos alunos e gerar rankings de desempenho. Seu objetivo é apoiar professores e coordenação no acompanhamento dos resultados de avaliações simuladas.

## Atores do domínio
- Coordenador
- Professor
- Aluno

## Linguagem onipresente
- Aluno: participante que realiza o simulado
- Professor: responsável por lançar e acompanhar notas
- Coordenador: responsável por gerenciar simulados e acompanhar resultados gerais
- Simulado: avaliação organizada pelo sistema
- Disciplina: componente avaliado dentro do simulado
- Nota: resultado obtido pelo aluno
- Ranking: classificação dos alunos com base no desempenho

## Regras de negócio principais
- Um simulado deve possuir pelo menos uma disciplina
- Cada disciplina pode possuir um peso
- A média final do aluno pode considerar pesos das disciplinas
- O ranking deve ordenar os alunos do maior para o menor desempenho

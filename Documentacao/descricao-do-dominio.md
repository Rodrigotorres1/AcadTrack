# Descrição do domínio

O AcadTrack é um sistema web voltado para o gerenciamento de simulados acadêmicos e acompanhamento do desempenho de alunos. O sistema foi projetado para apoiar coordenadores e professores na organização de avaliações e análise de resultados, além de permitir que alunos acompanhem seu desempenho.

---

# Domínios do sistema

## Domínio Principal (Core Domain)

O domínio principal do sistema é a Gestão Acadêmica, responsável pelas principais regras de negócio. Nesse domínio, estão incluídas as funcionalidades relacionadas à criação e gerenciamento de simulados, à definição de disciplinas e seus respectivos pesos, ao cálculo de médias e à geração de ranking de alunos. Esse domínio representa o núcleo do sistema, concentrando a lógica mais importante da aplicação.

---

## Domínio de Suporte

O domínio de suporte é o domínio de Avaliação, que atua no processamento das informações geradas no sistema. Nesse contexto, ele é responsável pelo processamento das notas, pelo cálculo das médias ponderadas e pela ordenação e classificação dos alunos com base no desempenho.

---

## Domínio Genérico

O domínio genérico do sistema é o domínio de Usuários, responsável pelo gerenciamento dos usuários e controle de acesso. Nesse domínio, são definidos os perfis de coordenador, professor e aluno, além das regras de autenticação e autorização que garantem que cada usuário acesse apenas as funcionalidades permitidas.

---

# Atores do sistema

Os principais atores do sistema são o coordenador, o professor e o aluno. O coordenador é responsável por cadastrar simulados, definir disciplinas e pesos, gerenciar turmas, visualizar o ranking geral e encerrar simulados. O professor atua no processo avaliativo, sendo responsável por lançar notas dos alunos, corrigir notas e acompanhar o desempenho das turmas. Já o aluno interage com o sistema principalmente para visualizar suas notas, acompanhar sua posição no ranking e analisar seu desempenho ao longo das avaliações.

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

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
- Solicitação de retificação: pedido de revisão de uma nota realizado pelo aluno

---

# Regras de negócio principais

- Um simulado deve possuir pelo menos uma disciplina associada para ser criado
- Cada disciplina associada a um simulado pode possuir um peso específico, que deve ser considerado no cálculo da média
- A média ponderada do aluno deve ser calculada com base nas notas obtidas em cada disciplina e nos respectivos pesos definidos
- O sistema deve permitir o lançamento de notas apenas para alunos vinculados à turma do simulado
- Um simulado finalizado não pode ser editado nem ter disciplinas ou pesos alterados
- O ranking deve ser gerado automaticamente com base na média ponderada dos alunos, ordenando do maior para o menor desempenho
- Em caso de empate no ranking, o sistema deve aplicar um critério de desempate (ex: maior nota na disciplina de maior peso)
- A solicitação de retificação de nota deve ser registrada e não altera automaticamente a nota, dependendo de validação do professor
- Apenas professores ou coordenadores podem alterar notas ou validar retificações
- Um aluno só pode estar vinculado a uma turma por vez no contexto de um mesmo período acadêmico
- O sistema deve garantir a integridade dos dados, impedindo inconsistências como notas sem aluno, disciplina ou simulado associado

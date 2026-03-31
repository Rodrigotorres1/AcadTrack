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

# Jornada do usuário

O fluxo principal do sistema ocorre de forma sequencial, iniciando com o coordenador, que cria um simulado e define suas disciplinas e pesos. Em seguida, os alunos são organizados em turmas para participar das avaliações. Após a realização dos simulados, o professor lança as notas dos alunos para cada disciplina. O sistema, então, realiza automaticamente o cálculo da média final ponderada com base nas regras definidas. A partir desses resultados, o sistema gera o ranking dos alunos considerando o desempenho. Por fim, o aluno acessa o sistema para visualizar seus resultados e sua posição no ranking.

---

# Linguagem onipresente

No contexto do sistema, alguns termos são fundamentais para a comunicação entre os envolvidos. O aluno é o participante que realiza o simulado. O professor é o responsável por lançar e acompanhar as notas. O coordenador é o responsável pela gestão acadêmica do sistema. O simulado representa a avaliação organizada no sistema, enquanto a disciplina corresponde ao componente avaliado. A nota é o resultado obtido pelo aluno, e o ranking é a classificação dos alunos com base no desempenho. Já a média ponderada é o cálculo da média considerando os pesos definidos para cada disciplina.

---

# Regras de negócio principais

O sistema segue algumas regras fundamentais para garantir seu funcionamento adequado. Todo simulado deve possuir pelo menos uma disciplina associada. Cada disciplina pode possuir um peso, que influencia diretamente no cálculo da média final. A média final deve considerar obrigatoriamente os pesos definidos. O ranking deve ordenar os alunos do maior para o menor desempenho. Além disso, um simulado encerrado não pode sofrer alterações, garantindo a integridade dos dados.

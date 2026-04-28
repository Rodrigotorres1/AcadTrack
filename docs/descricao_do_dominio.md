# Descricao do dominio

O AcadTrack e um sistema web voltado para o gerenciamento de simulados academicos e acompanhamento do desempenho de alunos. O sistema foi projetado para apoiar coordenadores e professores na organizacao de avaliacoes e analise de resultados, alem de permitir que alunos acompanhem seu desempenho.

---

# Dominios do sistema

## Dominio Principal (Core Domain)

O dominio principal do sistema e a Gestao Academica, responsavel pelas principais regras de negocio. Nesse dominio, estao incluidas as funcionalidades relacionadas a criacao e gerenciamento de simulados, a definicao de disciplinas e seus respectivos pesos, ao calculo de medias ponderadas e a geracao de ranking de alunos. Esse dominio representa o nucleo do sistema, concentrando a logica mais relevante e de maior valor para o negocio.

---

## Dominio de Suporte

O dominio de suporte e o dominio de Avaliacao, que atua no processamento das informacoes geradas no sistema. Nesse contexto, ele e responsavel pelo processamento das notas, pelo calculo das medias ponderadas e pela ordenacao e classificacao dos alunos com base no desempenho.

---

## Dominio Generico

O dominio generico do sistema e o dominio de Usuarios, responsavel pelo gerenciamento dos usuarios e controle de acesso. Nesse dominio, sao definidos os perfis de coordenador, professor e aluno, alem das regras de autenticacao e autorizacao que garantem que cada usuario acesse apenas as funcionalidades permitidas.

---

# Atores do sistema

Os principais atores do sistema sao o coordenador, o professor e o aluno. O coordenador e responsavel por cadastrar simulados, definir disciplinas e pesos, gerenciar turmas, visualizar o ranking geral e encerrar simulados. O professor atua no processo avaliativo, sendo responsavel por lancar notas dos alunos, corrigir notas e acompanhar o desempenho das turmas. Ja o aluno interage com o sistema principalmente para visualizar suas notas, acompanhar sua posicao no ranking e analisar seu desempenho ao longo das avaliacoes.

---

# Linguagem onipresente

Durante o desenvolvimento do sistema, foi adotada uma linguagem onipresente para garantir consistencia entre o dominio, o codigo e os testes.

- Aluno: participante que realiza o simulado
- Professor: responsavel por lancar e acompanhar notas
- Coordenador: responsavel pela gestao academica
- Simulado: avaliacao organizada no sistema
- Disciplina: componente avaliado
- Nota: resultado obtido pelo aluno
- Ranking: classificacao dos alunos com base no desempenho
- Media ponderada: calculo da media considerando pesos das disciplinas
- Solicitacao de retificacao: pedido de revisao de uma nota realizado pelo aluno

---

# Regras de negocio principais

- Um simulado deve possuir pelo menos uma disciplina associada para ser criado
- Cada disciplina associada a um simulado pode possuir um peso especifico, que deve ser considerado no calculo da media
- A media ponderada do aluno deve ser calculada com base nas notas obtidas em cada disciplina e nos respectivos pesos definidos
- O sistema deve permitir o lancamento de notas apenas para alunos vinculados a turma do simulado
- Um simulado finalizado nao pode ser editado nem ter disciplinas ou pesos alterados
- O ranking deve ser gerado automaticamente com base na media ponderada dos alunos, ordenando do maior para o menor desempenho
- Em caso de empate no ranking, o sistema deve aplicar um criterio de desempate (ex: maior nota na disciplina de maior peso)
- A solicitacao de retificacao de nota deve ser registrada com status inicial `PENDENTE`, nao alterando automaticamente a nota
- Apenas professores ou coordenadores podem alterar notas ou validar solicitacoes de retificacao
- Um aluno so pode estar vinculado a uma turma por vez no contexto de um mesmo periodo academico
- O sistema deve garantir a integridade dos dados, impedindo inconsistencias como notas sem aluno, disciplina ou simulado associado

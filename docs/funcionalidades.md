# Funcionalidades e Regras de Negocio

> **Medidas no sistema:** ao **lancar** ou **aprovar retificacao**, o cadastro do aluno usa **media global simples** e **situacao academica**. Consultas por simulado, ranking e parte da analise usam a composicao do simulado com peso padrao interno, sem tela ou endpoint para definicao manual de peso.

## 1. Lancar nota

**Regra de negocio:**
O professor pode lancar a nota de um aluno em uma disciplina de um simulado, desde que o valor informado esteja dentro do intervalo permitido pelo sistema.

**Por que nao e CRUD:**
Nao se trata apenas de salvar um numero no banco. O sistema aplica validacao sobre o valor da nota antes de registrar a informacao e **recalcula a media global simples e a situacao academica do aluno** apos o lancamento.

**Validacoes:**
- A nota deve estar entre 0 e 10
- A nota deve estar associada a um aluno, um simulado e uma disciplina
- Disciplina **ativa**; nao permitido segundo lancamento para o mesmo (aluno, simulado, disciplina)

---

## 2. Calcular media por simulado

**Regra de negocio:**
Para um **simulado concreto**, a media do aluno combina as notas obtidas nas disciplinas que compoem aquele simulado. Atualmente cada disciplina entra com peso padrao interno 1.0, pois a definicao manual de peso foi removida do escopo da entrega.

**Por que nao e CRUD:**
Essa funcionalidade exige processamento de dados, combinacao entre notas e composicao do simulado e aplicacao de formula de calculo, nao sendo apenas cadastro ou leitura de dados.

**Validacoes:**
- O aluno deve possuir notas vinculadas ao simulado
- As disciplinas devem pertencer a composicao do simulado
- Se nao houver composicao valida, a media por simulado nao pode ser calculada corretamente

---

## 3. Ranking academico como apoio da analise

**Regra de negocio:**
Os alunos sao ordenados para apoiar a analise academica. O ranking por simulado usa media por simulado; o ranking academico geral usa a media global persistida no aluno.

**Por que nao e CRUD:**
Essa funcionalidade exige calculo, comparacao e ordenacao dos alunos. Na Entrega 2 ela nao e tratada como funcionalidade principal separada, mas como apoio da analise de desempenho.

**Validacoes:**
- Apenas alunos com notas validas participam do ranking
- A ordenacao pode considerar media, desempenho ou risco academico
- A colecao ordenada e limitada com APIs nativas do Java na camada de aplicacao

---

## 4. Notificar responsavel sobre desempenho academico

**Regra de negocio:**
Depois de lancamento de nota ou aprovacao de retificacao, o sistema recalcula media, situacao e risco. Se houver risco academico, recuperacao ou destaque no Top 10, o responsavel vinculado recebe notificacao automatica.

**Por que nao e CRUD:**
A notificacao e uma consequencia de eventos de desempenho e usa Observer. Ela nao e uma funcionalidade independente: evolui os fluxos de notas e analise academica.

**Validacoes:**
- O aluno precisa ter responsavel vinculado e vinculo ativo
- O responsavel precisa receber a mensagem persistida
- A notificacao deve refletir risco, recuperacao ou destaque no ranking

---

## 5. Solicitar retificacao de nota

**Regra de negocio:**
O aluno pode solicitar a revisao de uma nota lancada, desde que informe uma justificativa para o pedido.

**Por que nao e CRUD:**
Alem de criar o registro, o sistema define automaticamente o estado inicial da solicitacao e exige justificativa obrigatoria.

**Validacoes:**
- A nota informada deve existir
- A justificativa e obrigatoria
- A solicitacao e criada com status inicial `PENDENTE`

---

## 6. Criar simulado com disciplinas vinculadas

**Regra de negocio:**
Um simulado so pode ser criado de forma valida quando possui disciplinas associadas a ele.

**Por que nao e CRUD:**
O sistema nao aceita um simulado "vazio", pois sua existencia depende de uma estrutura minima para representar a avaliacao.

**Validacoes:**
- O simulado deve possuir pelo menos uma disciplina vinculada
- O cadastro do simulado precisa respeitar a estrutura academica definida

---

## 7. Vincular aluno a turma

**Regra de negocio:**
Um aluno pode ser vinculado a uma turma, respeitando a organizacao academica do sistema.

**Por que nao e CRUD:**
Essa funcionalidade altera o contexto academico do aluno e representa uma associacao com impacto em outras funcionalidades, como participacao em simulados e organizacao por turma.

**Validacoes:**
- O aluno deve existir
- A turma deve existir
- O vinculo deve refletir corretamente a organizacao academica

---

## 8. Vincular responsavel ao aluno

**Regra de negocio:**
O sistema permite associar um responsavel a um aluno, registrando essa relacao de acompanhamento.

**Por que nao e CRUD:**
Essa operacao nao representa apenas cadastro isolado, mas a criacao de uma relacao entre entidades do dominio, com impacto na gestao do aluno.

**Validacoes:**
- O aluno deve existir
- O responsavel deve existir
- O vinculo deve ser registrado corretamente no sistema

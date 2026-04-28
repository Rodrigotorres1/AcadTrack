# Funcionalidades e Regras de Negocio

## 1. Lancar nota

**Regra de negocio:**
O professor pode lancar a nota de um aluno em uma disciplina de um simulado, desde que o valor informado esteja dentro do intervalo permitido pelo sistema.

**Por que nao e CRUD:**
Nao se trata apenas de salvar um numero no banco. O sistema aplica validacao sobre o valor da nota antes de registrar a informacao.

**Validacoes:**
- A nota deve estar entre 0 e 10
- A nota deve estar associada a um aluno, um simulado e uma disciplina

---

## 2. Calcular media ponderada

**Regra de negocio:**
A media do aluno e calculada com base nas notas obtidas e nos pesos definidos para cada disciplina do simulado.

**Por que nao e CRUD:**
Essa funcionalidade exige processamento de dados, combinacao entre notas e pesos e aplicacao de formula de calculo, nao sendo apenas cadastro ou leitura de dados.

**Validacoes:**
- O aluno deve possuir notas vinculadas ao simulado
- As disciplinas do simulado devem possuir pesos definidos
- Se nao houver pesos validos, a media nao pode ser calculada corretamente

---

## 3. Gerar ranking

**Regra de negocio:**
Os alunos sao ordenados com base no desempenho obtido no simulado, utilizando a media ponderada como criterio principal.

**Por que nao e CRUD:**
Essa funcionalidade exige calculo, comparacao e ordenacao dos alunos, produzindo uma classificacao baseada em regra de negocio.

**Validacoes:**
- Apenas alunos com notas validas participam do ranking
- A ordenacao deve ocorrer do maior para o menor desempenho

---

## 4. Solicitar retificacao de nota

**Regra de negocio:**
O aluno pode solicitar a revisao de uma nota lancada, desde que informe uma justificativa para o pedido.

**Por que nao e CRUD:**
Alem de criar o registro, o sistema define automaticamente o estado inicial da solicitacao e exige justificativa obrigatoria.

**Validacoes:**
- A nota informada deve existir
- A justificativa e obrigatoria
- A solicitacao e criada com status inicial `PENDENTE`

---

## 5. Definir peso de disciplina no simulado

**Regra de negocio:**
Cada disciplina associada a um simulado pode receber um peso especifico, que sera usado no calculo da media ponderada.

**Por que nao e CRUD:**
Nao e apenas associar uma disciplina ao simulado. O sistema precisa garantir que o peso informado seja valido e coerente com a regra do calculo posterior.

**Validacoes:**
- O peso deve ser maior que zero
- A disciplina deve estar vinculada ao simulado
- O peso sera considerado nos calculos de desempenho

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

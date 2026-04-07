# Funcionalidades e Regras de Negócio

## 1. Lançar nota

**Regra de negócio:**  
O professor pode lançar a nota de um aluno em uma disciplina de um simulado, desde que o valor informado esteja dentro do intervalo permitido pelo sistema.

**Por que não é CRUD:**  
Não se trata apenas de salvar um número no banco. O sistema aplica validação sobre o valor da nota antes de registrar a informação.

**Validações:**  
- A nota deve estar entre 0 e 10  
- A nota deve estar associada a um aluno, um simulado e uma disciplina

---

## 2. Calcular média ponderada

**Regra de negócio:**  
A média do aluno é calculada com base nas notas obtidas e nos pesos definidos para cada disciplina do simulado.

**Por que não é CRUD:**  
Essa funcionalidade exige processamento de dados, combinação entre notas e pesos e aplicação de fórmula de cálculo, não sendo apenas cadastro ou leitura de dados.

**Validações:**  
- O aluno deve possuir notas vinculadas ao simulado  
- As disciplinas do simulado devem possuir pesos definidos  
- Se não houver pesos válidos, a média não pode ser calculada corretamente

---

## 3. Gerar ranking

**Regra de negócio:**  
Os alunos são ordenados com base no desempenho obtido no simulado, utilizando a média ponderada como critério principal.

**Por que não é CRUD:**  
Essa funcionalidade exige cálculo, comparação e ordenação dos alunos, produzindo uma classificação baseada em regra de negócio.

**Validações:**  
- Apenas alunos com notas válidas participam do ranking  
- A ordenação deve ocorrer do maior para o menor desempenho

---

## 4. Solicitar retificação de nota

**Regra de negócio:**  
O aluno pode solicitar a revisão de uma nota lançada, desde que informe uma justificativa para o pedido.

**Por que não é CRUD:**  
Além de criar o registro, o sistema define automaticamente o estado inicial da solicitação e exige justificativa obrigatória.

**Validações:**  
- A nota informada deve existir  
- A justificativa é obrigatória  
- A solicitação é criada com status inicial `PENDENTE`

---

## 5. Definir peso de disciplina no simulado

**Regra de negócio:**  
Cada disciplina associada a um simulado pode receber um peso específico, que será usado no cálculo da média ponderada.

**Por que não é CRUD:**  
Não é apenas associar uma disciplina ao simulado. O sistema precisa garantir que o peso informado seja válido e coerente com a regra do cálculo posterior.

**Validações:**  
- O peso deve ser maior que zero  
- A disciplina deve estar vinculada ao simulado  
- O peso será considerado nos cálculos de desempenho

---

## 6. Criar simulado com disciplinas vinculadas

**Regra de negócio:**  
Um simulado só pode ser criado de forma válida quando possui disciplinas associadas a ele.

**Por que não é CRUD:**  
O sistema não aceita um simulado “vazio”, pois sua existência depende de uma estrutura mínima para representar a avaliação.

**Validações:**  
- O simulado deve possuir pelo menos uma disciplina vinculada  
- O cadastro do simulado precisa respeitar a estrutura acadêmica definida

---

## 7. Vincular aluno à turma

**Regra de negócio:**  
Um aluno pode ser vinculado a uma turma, respeitando a organização acadêmica do sistema.

**Por que não é CRUD:**  
Essa funcionalidade altera o contexto acadêmico do aluno e representa uma associação com impacto em outras funcionalidades, como participação em simulados e organização por turma.

**Validações:**  
- O aluno deve existir  
- A turma deve existir  
- O vínculo deve refletir corretamente a organização acadêmica

---

## 8. Vincular responsável ao aluno

**Regra de negócio:**  
O sistema permite associar um responsável a um aluno, registrando essa relação de acompanhamento.

**Por que não é CRUD:**  
Essa operação não representa apenas cadastro isolado, mas a criação de uma relação entre entidades do domínio, com impacto na gestão do aluno.

**Validações:**  
- O aluno deve existir  
- O responsável deve existir  
- O vínculo deve ser registrado corretamente no sistema

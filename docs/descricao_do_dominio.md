# Descricao do dominio

O AcadTrack e um sistema web voltado para o gerenciamento de simulados academicos e acompanhamento do desempenho de alunos. O sistema foi projetado para apoiar coordenadores e professores na organizacao de avaliacoes e analise de resultados, alem de permitir que alunos acompanhem seu desempenho.

---

# Dominios do sistema

## Dominio Principal (Core Domain)

O dominio principal do sistema e a Gestao Academica, responsavel pelas principais regras de negocio. Incluem-se a criacao e gestao de simulados, composicao por disciplinas, consultas de media por simulado, ranking de apoio e acompanhamento do desempenho dentro dessa estrutura. Ja a sintese da **situacao do aluno no cadastro** deriva da **media global simples** de todas as notas (detalhes na secao "Medidas de desempenho"). Esse nucleo agrega valor ao negocio da instituicao.

---

## Dominio de Suporte

O dominio de suporte e o dominio de Avaliacao, que atua no processamento das informacoes avaliativas. Inclui o lancamento e retificacao de notas e o uso da **media global simples** para atualizar a situacao registada no aluno, alem das operacoes de media por simulado e ordenacao relativas ao desempenho.

---

## Dominio Generico

O dominio generico do sistema e o dominio de Usuarios, responsavel pelo gerenciamento dos usuarios e controle de acesso. Nesse dominio, sao definidos os perfis de coordenador, professor e aluno, alem das regras de autenticacao e autorizacao que garantem que cada usuario acesse apenas as funcionalidades permitidas.

---

# Atores do sistema

Os principais atores do sistema sao o coordenador, o professor e o aluno. O coordenador e responsavel por cadastrar simulados, selecionar disciplinas, gerenciar turmas e visualizar rankings. O professor atua no processo avaliativo, lancando notas e acompanhando turmas. O aluno acompanha notas, ranking e desempenho ao longo das avaliacoes (no modelo atual a consulta autosservico pode concentrar-se nas APIs e fluxos expostos aos responsaveis conforme documentacao).

---

# Linguagem onipresente

Durante o desenvolvimento do sistema, foi adotada uma linguagem onipresente para garantir consistencia entre o dominio, o codigo e os testes.

- Aluno: participante que realiza o simulado
- Professor: responsavel por lancar e acompanhar notas
- Coordenador: responsavel pela gestao academica
- Simulado: avaliacao organizada no sistema
- Disciplina: componente avaliado
- Nota: resultado obtido pelo aluno num par (simulado, disciplina)
- Ranking (por simulado): ordenacao dos alunos naquele simulado pela media da composicao
- **Media global simples**: media aritmetica de todas as notas do aluno; atualiza media e **situacao academica** registadas no cadastro do aluno
- **Media por simulado**: media do aluno restrita a um simulado, usando as disciplinas da composicao com peso padrao interno
- Solicitacao de retificacao: pedido de revisao de uma nota sujeito ao fluxo de estados

---

# Medidas de desempenho (duas definicoes complementares)

Para evitar ambiguidade na avaliacao academica:

1. **Media global simples** — cada nota do aluno conta igual para uma media aritmetica global; essa media e a base para **APROVADO / RECUPERACAO / REPROVADO** persistidos no agregado **Aluno** apos **lancar nota** ou **aprovar retificacao**.
2. **Media por simulado** — para cada simulado, as notas nas disciplinas da composicao sao combinadas com peso padrao interno; serve para consulta explicita de media no simulado, ranking daquele simulado e historico por simulado na analise de desempenho consolidada.

Estas duas medidas coexistem de forma **intencional**: a primeira sintetiza trajetoria amplia do **aluno**; a segunda reflete a estrutura pedagogica de cada avaliacao simulada.

---

# Regras de negocio principais (alinhadas a implementacao atual)

- Um **simulado** e criado com **pelo menos duas disciplinas distintas**, todas existentes e **sem repeticao** na mesma composicao; a descricao do simulado e **unica** (comparacao normalizada).
- Disciplinas compoem o simulado com peso padrao interno, sem configuracao manual de peso na interface ou em endpoint separado.
- **Media por simulado**: calculada somente no ambito desse simulado (notas do aluno nas disciplinas da composicao).
- **Media global simples** e **situacao academica** no cadastro do **Aluno**: apos **lancamento de nota** ou **aprovacao de retificacao**, recalculam-se a media aritmetica de **todas** as notas do aluno e a situacao (>= 7 aprovado; >= 5 recuperacao; senao reprovado).
- **Nota**: valor entre 0 e 10; nao e permitido segundo lancamento para o mesmo **(aluno, simulado, disciplina)**; **disciplina inativa** nao recebe lancamento.
- **Ranking por simulado**: ordenacao pela media por simulado **decrescente**; empates ficam na ordem em que o conjunto e montado (sem criterio adicional de desempate no codigo atual).
- **Solicitacao de retificacao**: registrada como `PENDENTE`; transicoes ate decisao; na **aprovacao**, a nota e alterada e dispara o mesmo recalculo da media global simples / situacao no aluno.
- Integridade referencial: notas referenciam aluno, simulado e disciplina existentes (validado nos casos de uso).

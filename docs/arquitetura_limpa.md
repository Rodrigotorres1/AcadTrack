# Arquitetura Limpa

O sistema AcadTrack foi estruturado seguindo os principios da Arquitetura Limpa, organizando o codigo em camadas bem definidas e com responsabilidades especificas. Essa abordagem garante separacao de responsabilidades, baixo acoplamento e maior facilidade de manutencao.

**Maven e pacotes Java:** o agregador multi-modulo usa `groupId` `g8`, artifactId do pai **`AcadTrack-pai`**, versão **`0.0.1-SNAPSHOT`** (alinhamento ao modelo da disciplina); os tipos ficam sob o namespace `g8.acadtrack.*`, com raiz física em `src/.../g8/acadtrack/`.

## Camadas

### Dominio
A camada de dominio concentra as regras de negocio e os conceitos centrais do sistema, sendo independente de frameworks e tecnologias externas.

Modulos:
- dominio-academico
- dominio-avaliacao
- dominio-usuarios
- dominio-compartilhado

Nessa camada estao presentes entidades como Aluno, Nota, Simulado, Disciplina e Solicitacao de Retificacao, alem das interfaces de repositorio que definem contratos de acesso aos dados.

---

### Aplicacao
A camada de aplicacao e responsavel por orquestrar o fluxo do sistema por meio dos casos de uso, utilizando as regras definidas no dominio.

Exemplos de casos de uso:
- Lancar nota
- Calcular media ponderada
- Gerar ranking
- Solicitar retificacao de nota

Essa camada coordena as operacoes do sistema sem depender diretamente de detalhes de infraestrutura.

---

### Infraestrutura
A camada de infraestrutura implementa os detalhes tecnicos necessarios para o funcionamento do sistema, especialmente a persistencia de dados.

Principais responsabilidades:
- Implementacao de repositorios com Spring Data JPA
- Mapeamento de entidades para o banco de dados
- Integracao com tecnologias externas

Essa camada depende das interfaces definidas no dominio.

---

### Apresentacao
A camada de apresentacao e responsavel pela comunicacao com o mundo externo por meio da API REST.

Componentes:
- Controllers
- Endpoints HTTP
- DTOs de entrada e saida

Essa camada recebe requisicoes, valida dados e aciona os casos de uso da aplicacao.

---

### Testes BDD
O modulo `bdd/acadtrackbdd` contem os testes automatizados utilizando Cucumber.

Nele estao:
- Arquivos `.feature` com cenarios
- Steps que executam casos de uso reais
- Configuracao de integracao com Spring

Essa camada valida o comportamento do sistema a partir da perspectiva do usuario.

---

## Beneficios

- Separacao clara de responsabilidades
- Baixo acoplamento entre camadas
- Alta coesao dentro dos modulos
- Facilidade de manutencao e evolucao do sistema
- Testabilidade aprimorada com suporte a BDD

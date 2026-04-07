# Arquitetura Limpa

O sistema AcadTrack foi estruturado seguindo os princípios da Arquitetura Limpa, organizando o código em camadas bem definidas e com responsabilidades específicas. Essa abordagem garante separação de responsabilidades, baixo acoplamento e maior facilidade de manutenção.

## Camadas

### Domínio
A camada de domínio concentra as regras de negócio e os conceitos centrais do sistema, sendo independente de frameworks e tecnologias externas.

Módulos:
- dominio-academico
- dominio-avaliacao
- dominio-usuarios
- dominio-compartilhado

Nessa camada estão presentes entidades como Aluno, Nota, Simulado, Disciplina e Solicitação de Retificação, além das interfaces de repositório que definem contratos de acesso aos dados.

---

### Aplicação
A camada de aplicação é responsável por orquestrar o fluxo do sistema por meio dos casos de uso, utilizando as regras definidas no domínio.

Exemplos de casos de uso:
- Lançar nota
- Calcular média ponderada
- Gerar ranking
- Solicitar retificação de nota

Essa camada coordena as operações do sistema sem depender diretamente de detalhes de infraestrutura.

---

### Infraestrutura
A camada de infraestrutura implementa os detalhes técnicos necessários para o funcionamento do sistema, especialmente a persistência de dados.

Principais responsabilidades:
- Implementação de repositórios com Spring Data JPA
- Mapeamento de entidades para o banco de dados
- Integração com tecnologias externas

Essa camada depende das interfaces definidas no domínio.

---

### Apresentação
A camada de apresentação é responsável pela comunicação com o mundo externo por meio da API REST.

Componentes:
- Controllers
- Endpoints HTTP
- DTOs de entrada e saída

Essa camada recebe requisições, valida dados e aciona os casos de uso da aplicação.

---

### Testes BDD
O módulo `bdd/acadtrackbdd` contém os testes automatizados utilizando Cucumber.

Nele estão:
- Arquivos `.feature` com cenários
- Steps que executam casos de uso reais
- Configuração de integração com Spring

Essa camada valida o comportamento do sistema a partir da perspectiva do usuário.

---

## Benefícios

- Separação clara de responsabilidades
- Baixo acoplamento entre camadas
- Alta coesão dentro dos módulos
- Facilidade de manutenção e evolução do sistema
- Testabilidade aprimorada com suporte a BDD

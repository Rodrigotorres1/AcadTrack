# Arquitetura Limpa

## Organização
- Documentacao  
  Contém a descrição do domínio, linguagem onipresente, regras de negócio, mapa de histórias e demais artefatos da entrega.

- acadtrack.cml  
  Arquivo de modelagem de contexto, representando os subdomínios e suas relações.

- bdd  
  Contém os cenários de teste em Gherkin e a automação com Cucumber.

- dominio-principal  
  Contém as entidades, regras de negócio e serviços centrais do sistema.

- aplicacao  
  Contém os casos de uso e a coordenação das operações do sistema.

- infraestrutura  
  Contém a persistência em banco relacional, integração com JPA e demais recursos externos.

- apresentacao  
  Contém a interface de entrada e saída do sistema, como controllers e interface web.

## Camadas da arquitetura

### Domínio
Contém as entidades, regras de negócio e serviços de domínio. Essa camada é o núcleo do sistema e não deve depender de frameworks ou tecnologias externas.

### Aplicação
Responsável por orquestrar os casos de uso e coordenar o fluxo entre domínio, infraestrutura e apresentação.

### Infraestrutura
Responsável pela persistência de dados, acesso ao banco relacional e integração com frameworks e bibliotecas externas.

### Apresentação
Responsável pela comunicação com o usuário, incluindo interface web, controllers e entrada/saída de dados.

## Princípios aplicados
- Separação de responsabilidades
- Independência de frameworks
- Foco nas regras de negócio
- Inversão de dependência

## Benefícios
- Facilidade de manutenção
- Maior organização do código
- Melhor testabilidade
- Flexibilidade para evolução do sistema

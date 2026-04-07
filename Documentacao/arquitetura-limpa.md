# Arquitetura Limpa

O sistema foi estruturado seguindo os princípios da arquitetura limpa.

## Camadas

### Domínio
Contém as regras de negócio e entidades.

Módulos:
- dominio-academico
- dominio-avaliacao
- dominio-usuarios
- dominio-compartilhado

### Aplicação
Contém os casos de uso.

Exemplo:
- Calcular média ponderada
- Gerar ranking
- Solicitar retificação

### Infraestrutura
Responsável pela persistência de dados.

- JPA
- Repositórios Spring Data

### Apresentação
Camada responsável pela exposição da API REST.

- Controllers
- Endpoints HTTP

## Benefícios

- Baixo acoplamento
- Alta coesão
- Facilidade de manutenção

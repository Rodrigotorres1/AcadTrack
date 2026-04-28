# BDD - Behavior Driven Development

O sistema utiliza BDD para validar o comportamento das funcionalidades.

## Ferramentas utilizadas

- Cucumber
- JUnit

## Estrutura

- Arquivos `.feature` descrevem cenarios de negocio
- Steps implementam os testes automatizados

## Exemplo de cenario

Feature: Solicitar retificacao de nota

Scenario: Solicitar retificacao com justificativa valida
  Dado que o aluno possui uma nota
  Quando ele solicita retificacao com justificativa
  Entao o sistema registra a solicitacao com status PENDENTE

## Objetivo

Garantir que o sistema funcione conforme os requisitos definidos.

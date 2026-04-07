# BDD - Behavior Driven Development

O sistema utiliza BDD para validar o comportamento das funcionalidades.

## Ferramentas utilizadas

- Cucumber
- JUnit

## Estrutura

- Arquivos `.feature` descrevem cenários de negócio
- Steps implementam os testes automatizados

## Exemplo de cenário

Feature: Solicitar retificação de nota

Scenario: Solicitar retificação com justificativa válida
  Dado que o aluno possui uma nota
  Quando ele solicita retificação com justificativa
  Então o sistema registra a solicitação com status PENDENTE

## Objetivo

Garantir que o sistema funcione conforme os requisitos definidos.

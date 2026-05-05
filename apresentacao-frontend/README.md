# Camada de apresentação web

Esta pasta representa a camada de apresentação web do AcadTrack.

Os arquivos principais da interface são:

- `index.html`
- `styles.css`
- `app.js`

A interface consome os endpoints REST do backend e não acessa banco de dados, repositories ou regras de negócio diretamente.

Para que o Spring Boot continue publicando a tela automaticamente pelo navegador, os mesmos arquivos também são mantidos em:

```text
apresentacao-backend/src/main/resources/static/
```

Assim, `apresentacao-frontend/` documenta e organiza a camada web dentro da estrutura do projeto, enquanto `static/` mantém a publicação direta pela aplicação Spring Boot.

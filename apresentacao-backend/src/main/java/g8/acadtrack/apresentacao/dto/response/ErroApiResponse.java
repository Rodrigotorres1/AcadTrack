package g8.acadtrack.apresentacao.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Documentação OpenAPI para o formato de erro retornado por {@link g8.acadtrack.apresentacao.exception.GlobalExceptionHandler}.
 */
@Schema(description = """
        Corpo de erro padronizado (timestamp UTC, código HTTP, frase oficial e mensagem do domínio).""")
public record ErroApiResponse(
        @Schema(example = "2026-04-29T18:45:33.561Z") String timestamp,
        @Schema(description = "Código HTTP.", example = "404") Integer status,
        @Schema(description = "Razão curta RFC 7231.", example = "Not Found") String error,
        @Schema(description = "Mensagem contextual da exceção.", example = "Aluno não encontrado.") String message
) {
}

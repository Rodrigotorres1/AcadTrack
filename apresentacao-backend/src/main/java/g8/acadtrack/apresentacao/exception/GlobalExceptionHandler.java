package g8.acadtrack.apresentacao.exception;

import g8.acadtrack.dominiocompartilhado.excecao.AcessoDenegadoException;
import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.excecao.EntidadeNaoEncontradaException;
import g8.acadtrack.dominiocompartilhado.excecao.RegraDeNegocioException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntidadeNaoEncontradaException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(RegraDeNegocioException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ConflitoDeEstadoException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflitoDeEstadoException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AcessoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(AcessoDenegadoException ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Valor inválido")
                .distinct()
                .toList();
        return buildValidation(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage() != null ? violation.getMessage() : "Valor inválido")
                .distinct()
                .toList();
        return buildValidation(errors);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String mensagem) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensagem);
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildValidation(List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Erro de validação");
        body.put("errors", errors.isEmpty() ? List.of("Dados inválidos") : errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}

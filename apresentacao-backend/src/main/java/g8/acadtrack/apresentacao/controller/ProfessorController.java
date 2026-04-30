package g8.acadtrack.apresentacao.controller;

import g8.acadtrack.aplicacao.professor.CriarProfessorUseCase;
import g8.acadtrack.apresentacao.dto.request.CriarProfessorRequest;
import g8.acadtrack.apresentacao.dto.response.ErroApiResponse;
import g8.acadtrack.apresentacao.dto.response.ProfessorResponse;
import g8.acadtrack.dominiousuarios.professor.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Professores", description = "Cadastro de professores.")
@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final CriarProfessorUseCase criarProfessorUseCase;

    public ProfessorController(CriarProfessorUseCase criarProfessorUseCase) {
        this.criarProfessorUseCase = criarProfessorUseCase;
    }

    @Operation(summary = "Cadastrar professor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(schema = @Schema(implementation = ProfessorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(@RequestBody @Valid CriarProfessorRequest request) {
        Professor professor = criarProfessorUseCase.executar(
                request.getNome(),
                request.getEmail()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProfessorResponse.fromDomain(professor));
    }
}
package g8.acadtrack.bdd.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.aluno.InativarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.aluno.SituacaoAcademica;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.nota.Nota;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class LancarNotaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final InativarAlunoUseCase inativarAlunoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;
    private final AlunoRepository alunoRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private Aluno aluno;
    private Disciplina disciplina;
    private Disciplina segundaDisciplina;
    private Simulado simulado;
    private Simulado segundoSimulado;
    private Nota notaLancada;
    private MvcResult respostaApi;
    private Exception excecao;

    public LancarNotaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            InativarAlunoUseCase inativarAlunoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            CriarSimuladoUseCase criarSimuladoUseCase,
            LancarNotaUseCase lancarNotaUseCase,
            AlunoRepository alunoRepository,
            MockMvc mockMvc,
            ObjectMapper objectMapper
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.inativarAlunoUseCase = inativarAlunoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
        this.alunoRepository = alunoRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Dado("que o aluno {string} realizou o simulado")
    public void queOAlunoRealizouOSimulado(String nomeAluno) {
        context.resetMensagens();
        excecao = null;
        notaLancada = null;
        respostaApi = null;

        aluno = criarAlunoUseCase.executar(nomeAluno, emailSeguro(nomeAluno) + "@email.com");
        disciplina = criarDisciplinaUseCase.executar("Matemática " + nomeAluno);
        Disciplina fillerDisciplina = criarDisciplinaUseCase.executar("História " + nomeAluno);

        simulado = criarSimuladoUseCase.executar(
                "Simulado de lançamento de nota " + nomeAluno,
                List.of(disciplina.getId(), fillerDisciplina.getId())
        );
    }

    @Dado("que o aluno {string} está inativo e realizou o simulado")
    public void queOAlunoEstaInativoERealizouOSimulado(String nomeAluno) {
        queOAlunoRealizouOSimulado(nomeAluno);
        aluno = inativarAlunoUseCase.executar(aluno.getId());
    }

    @Dado("que o aluno {string} possui nota {double} já lançada")
    public void queOAlunoPossuiNotaJaLancada(String nomeAluno, Double valorNota) {
        queOAlunoRealizouOSimulado(nomeAluno);
        notaLancada = lancarNotaUseCase.executar(
                aluno.getId(),
                simulado.getId(),
                disciplina.getId(),
                valorNota
        );

        segundaDisciplina = criarDisciplinaUseCase.executar("Português " + nomeAluno);
        Disciplina fillerSegundoDisciplina = criarDisciplinaUseCase.executar("Biologia " + nomeAluno);
        segundoSimulado = criarSimuladoUseCase.executar(
                "Simulado complementar " + nomeAluno,
                List.of(segundaDisciplina.getId(), fillerSegundoDisciplina.getId())
        );
    }

    @Quando("o professor lança a nota {double} para o aluno {string}")
    public void oProfessorLancaANotaParaOAluno(Double valorNota, String nomeAluno) {
        try {
            notaLancada = lancarNotaUseCase.executar(
                    aluno.getId(),
                    simulado.getId(),
                    disciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor já lançou a nota {double} para o aluno {string}")
    public void oProfessorJaLancouANotaParaOAluno(Double valorNota, String nomeAluno) {
        oProfessorLancaANotaParaOAluno(valorNota, nomeAluno);
        assertTrue(context.isOperacaoExecutada());
        assertNotNull(notaLancada);
    }

    @Quando("o professor lança via API a nota {double} para o aluno {string}")
    public void oProfessorLancaViaApiANotaParaOAluno(Double valorNota, String nomeAluno) {
        String body = "{\"alunoId\":" + aluno.getId()
                + ",\"simuladoId\":" + simulado.getId()
                + ",\"disciplinaId\":" + disciplina.getId()
                + ",\"valor\":" + valorNota
                + "}";

        try {
            respostaApi = mockMvc.perform(post("/notas")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body))
                    .andReturn();
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor tenta lançar a nota {double} novamente para o mesmo aluno e disciplina")
    public void oProfessorTentaLancarANotaNovamenteParaOMesmoAlunoEDisciplina(Double valorNota) {
        try {
            lancarNotaUseCase.executar(
                    aluno.getId(),
                    simulado.getId(),
                    disciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("o professor lança uma nova nota {double} para o aluno {string} em outra disciplina")
    public void oProfessorLancaUmaNovaNotaParaOAlunoEmOutraDisciplina(Double valorNota, String nomeAluno) {
        try {
            notaLancada = lancarNotaUseCase.executar(
                    aluno.getId(),
                    segundoSimulado.getId(),
                    segundaDisciplina.getId(),
                    valorNota
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra a nota do aluno")
    public void oSistemaRegistraANotaDoAluno() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(notaLancada);
    }

    @Então("a API retorna a nota criada sem nome da disciplina e descrição do simulado")
    public void aApiRetornaANotaCriadaSemNomeDaDisciplinaEDescricaoDoSimulado() throws Exception {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(respostaApi);
        assertEquals(201, respostaApi.getResponse().getStatus());

        JsonNode response = objectMapper.readTree(respostaApi.getResponse().getContentAsString());
        assertEquals(aluno.getId(), response.get("alunoId").asLong());
        assertEquals(simulado.getId(), response.get("simuladoId").asLong());
        assertEquals(disciplina.getId(), response.get("disciplinaId").asLong());
        assertTrue(response.get("nomeDisciplina").isNull());
        assertTrue(response.get("descricaoSimulado").isNull());
    }

    @Então("o sistema informa a nota deve estar entre 0 e 10")
    public void oSistemaInformaQueANotaEInvalida() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("A nota deve estar entre 0 e 10", context.getMensagem());
    }

    @Então("o sistema informa que aluno inativo não pode receber nota")
    public void oSistemaInformaQueAlunoInativoNaoPodeReceberNota() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Aluno inativo não pode receber lançamento de nota", context.getMensagem());
    }

    @Então("o sistema informa que já existe nota lançada para este aluno, simulado e disciplina")
    public void oSistemaInformaDuplicidadeDeNota() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe nota lançada para este aluno, simulado e disciplina", context.getMensagem());
    }

    @Então("o sistema atualiza a média do aluno para {double}")
    public void oSistemaAtualizaAMediaDoAlunoPara(Double mediaEsperada) {
        Aluno alunoAtualizado = alunoRepository.buscarPorId(aluno.getId())
                .orElseThrow(() -> new AssertionError("Aluno não encontrado após lançamento de nota"));

        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(mediaEsperada, alunoAtualizado.getMediaAritmetica(), 0.001);
    }

    @Então("o sistema atualiza a situação acadêmica do aluno para {string}")
    public void oSistemaAtualizaASituacaoAcademicaDoAlunoPara(String situacaoEsperada) {
        Aluno alunoAtualizado = alunoRepository.buscarPorId(aluno.getId())
                .orElseThrow(() -> new AssertionError("Aluno não encontrado após lançamento de nota"));

        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertEquals(SituacaoAcademica.valueOf(situacaoEsperada), alunoAtualizado.getSituacaoAcademica());
    }

    private String emailSeguro(String nomeAluno) {
        return nomeAluno.toLowerCase().replace(" ", ".");
    }
}

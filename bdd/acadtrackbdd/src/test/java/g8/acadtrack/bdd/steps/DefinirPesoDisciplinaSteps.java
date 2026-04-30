package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.aplicacao.simulado.VincularDisciplinaSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplina;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DefinirPesoDisciplinaSteps {

    private final TestContext context;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase;

    private Simulado simulado;
    private Disciplina disciplina;
    private SimuladoDisciplina simuladoDisciplina;
    private Exception excecao;

    public DefinirPesoDisciplinaSteps(
            TestContext context,
            CriarSimuladoUseCase criarSimuladoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            VincularDisciplinaSimuladoUseCase vincularDisciplinaSimuladoUseCase
    ) {
        this.context = context;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.vincularDisciplinaSimuladoUseCase = vincularDisciplinaSimuladoUseCase;
    }

    @Dado("que o simulado possui a disciplina {string}")
    public void queOSimuladoPossuiADisciplina(String nomeDisciplina) {
        context.resetMensagens();
        excecao = null;
        simuladoDisciplina = null;

        Disciplina disciplinaBase = criarDisciplinaUseCase.executar("Disciplina Base");
        simulado = criarSimuladoUseCase.executar(
 "Simulado de teste",
            List.of(disciplinaBase.getId())
);
        disciplina = criarDisciplinaUseCase.executar(nomeDisciplina);
    }

    @Quando("o coordenador define o peso {double} para a disciplina {string}")
    public void oCoordenadorDefineOPesoParaADisciplina(Double peso, String disciplinaNome) {
        try {
            simuladoDisciplina = vincularDisciplinaSimuladoUseCase.executar(
                    simulado.getId(),
                    disciplina.getId(),
                    peso
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra o peso da disciplina corretamente")
    public void oSistemaRegistraOPesoDaDisciplinaCorretamente() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(simuladoDisciplina);
        assertEquals(simulado.getId(), simuladoDisciplina.getSimuladoId());
        assertEquals(disciplina.getId(), simuladoDisciplina.getDisciplinaId());
    }

    @Então("o sistema informa que o peso deve ser válido")
    public void oSistemaInformaQueOPesoDeveSerValido() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Peso deve ser maior que zero", context.getMensagem());
    }
}
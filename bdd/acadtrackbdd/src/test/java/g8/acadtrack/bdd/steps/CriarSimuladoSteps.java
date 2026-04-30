package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.disciplina.Disciplina;
import g8.acadtrack.dominioavaliacao.simulado.Simulado;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CriarSimuladoSteps {

    private final TestContext context;
    private final CriarSimuladoUseCase criarSimuladoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;

    private final List<Long> disciplinasIds = new ArrayList<>();
    private Simulado simulado;
    private Exception excecao;
    private String descricaoSimulado;

    public CriarSimuladoSteps(
            TestContext context,
            CriarSimuladoUseCase criarSimuladoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase
    ) {
        this.context = context;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
    }

    @Dado("que o coordenador deseja criar um simulado")
    public void queOCoordenadorDesejaCriarUmSimulado() {
        context.resetMensagens();
        disciplinasIds.clear();
        simulado = null;
        excecao = null;
        descricaoSimulado = "Simulado Principal " + UUID.randomUUID();
    }

    @Quando("ele informa as disciplinas {string} e {string}")
    public void eleInformaAsDisciplinasE(String d1, String d2) {
        try {
            Disciplina disciplina1 = criarDisciplinaUseCase.executar(d1);
            Disciplina disciplina2 = criarDisciplinaUseCase.executar(d2);

            disciplinasIds.add(disciplina1.getId());
            disciplinasIds.add(disciplina2.getId());

            simulado = criarSimuladoUseCase.executar(descricaoSimulado, disciplinasIds);

            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele não informa nenhuma disciplina")
    public void eleNaoInformaNenhumaDisciplina() {
        try {
            simulado = criarSimuladoUseCase.executar(descricaoSimulado, List.of());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele informa apenas a disciplina {string}")
    public void eleInformaApenasADisciplina(String disciplinaNome) {
        try {
            Disciplina disciplina = criarDisciplinaUseCase.executar(disciplinaNome);
            simulado = criarSimuladoUseCase.executar(descricaoSimulado, List.of(disciplina.getId()));
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele informa a mesma disciplina {string} duas vezes")
    public void eleInformaAMesmaDisciplinaDuasVezes(String disciplinaNome) {
        try {
            Disciplina disciplina = criarDisciplinaUseCase.executar(disciplinaNome);
            simulado = criarSimuladoUseCase.executar(
                    descricaoSimulado,
                    List.of(disciplina.getId(), disciplina.getId())
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele informa uma disciplina inexistente na composição")
    public void eleInformaUmaDisciplinaInexistenteNaComposicao() {
        try {
            Disciplina disciplina = criarDisciplinaUseCase.executar("Biologia");
            simulado = criarSimuladoUseCase.executar(descricaoSimulado, List.of(disciplina.getId(), 999999L));
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Dado("que já existe um simulado com descrição {string}")
    public void queJaExisteUmSimuladoComDescricao(String descricao) {
        queOCoordenadorDesejaCriarUmSimulado();
        Disciplina disciplina1 = criarDisciplinaUseCase.executar("Física");
        Disciplina disciplina2 = criarDisciplinaUseCase.executar("Química");
        criarSimuladoUseCase.executar(descricao, List.of(disciplina1.getId(), disciplina2.getId()));
        descricaoSimulado = descricao;
    }

    @Quando("ele tenta criar outro simulado com descrição {string}")
    public void eleTentaCriarOutroSimuladoComDescricao(String descricao) {
        try {
            Disciplina disciplina1 = criarDisciplinaUseCase.executar("Geografia");
            Disciplina disciplina2 = criarDisciplinaUseCase.executar("História");
            simulado = criarSimuladoUseCase.executar(descricao, List.of(disciplina1.getId(), disciplina2.getId()));
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema cria o simulado com as disciplinas informadas")
    public void oSistemaCriaOSimuladoComAsDisciplinasInformadas() {
        assertTrue(context.isOperacaoExecutada());
        assertNull(excecao);
        assertNotNull(simulado);
    }

    @Então("o sistema informa que o simulado deve possuir pelo menos uma disciplina")
    public void oSistemaInformaQueOSimuladoDevePossuirPeloMenosUmaDisciplina() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("O simulado deve possuir pelo menos uma disciplina", context.getMensagem());
    }

    @Então("o sistema informa que o simulado deve possuir pelo menos duas disciplinas distintas")
    public void oSistemaInformaQueOSimuladoDevePossuirPeloMenosDuasDisciplinasDistintas() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("O simulado deve possuir pelo menos duas disciplinas distintas", context.getMensagem());
    }

    @Então("o sistema informa que não é permitido vincular disciplina repetida no mesmo simulado")
    public void oSistemaInformaQueNaoEPermitidoVincularDisciplinaRepetidaNoMesmoSimulado() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Não é permitido vincular disciplina repetida no mesmo simulado", context.getMensagem());
    }

    @Então("o sistema informa que uma ou mais disciplinas não existem")
    public void oSistemaInformaQueUmaOuMaisDisciplinasNaoExistem() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Uma ou mais disciplinas não existem", context.getMensagem());
    }

    @Então("o sistema informa que já existe simulado cadastrado com esta descrição")
    public void oSistemaInformaQueJaExisteSimuladoCadastradoComEstaDescricao() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Já existe simulado cadastrado com esta descrição", context.getMensagem());
    }
}
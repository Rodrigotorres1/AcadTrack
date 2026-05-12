package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.disciplina.CriarDisciplinaUseCase;
import g8.acadtrack.aplicacao.disciplina.InativarDisciplinaUseCase;
import g8.acadtrack.aplicacao.nota.LancarNotaUseCase;
import g8.acadtrack.aplicacao.simulado.AtualizarSimuladoUseCase;
import g8.acadtrack.aplicacao.simulado.CriarSimuladoUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
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
    private final AtualizarSimuladoUseCase atualizarSimuladoUseCase;
    private final CriarDisciplinaUseCase criarDisciplinaUseCase;
    private final InativarDisciplinaUseCase inativarDisciplinaUseCase;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final LancarNotaUseCase lancarNotaUseCase;

    private final List<Long> disciplinasIds = new ArrayList<>();
    private Simulado simulado;
    private Exception excecao;
    private String descricaoSimulado;

    public CriarSimuladoSteps(
            TestContext context,
            CriarSimuladoUseCase criarSimuladoUseCase,
            AtualizarSimuladoUseCase atualizarSimuladoUseCase,
            CriarDisciplinaUseCase criarDisciplinaUseCase,
            InativarDisciplinaUseCase inativarDisciplinaUseCase,
            CriarAlunoUseCase criarAlunoUseCase,
            LancarNotaUseCase lancarNotaUseCase
    ) {
        this.context = context;
        this.criarSimuladoUseCase = criarSimuladoUseCase;
        this.atualizarSimuladoUseCase = atualizarSimuladoUseCase;
        this.criarDisciplinaUseCase = criarDisciplinaUseCase;
        this.inativarDisciplinaUseCase = inativarDisciplinaUseCase;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.lancarNotaUseCase = lancarNotaUseCase;
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

    @Dado("que existe um simulado com nota lancada")
    public void queExisteUmSimuladoComNotaLancada() {
        queOCoordenadorDesejaCriarUmSimulado();
        String sufixo = UUID.randomUUID().toString();
        Aluno aluno = criarAlunoUseCase.executar(
                "Aluno Simulado Bloqueado " + sufixo,
                "aluno.simulado.bloqueado." + sufixo + "@email.com"
        );
        Disciplina disciplina1 = criarDisciplinaUseCase.executar("Disciplina Bloqueada A " + sufixo);
        Disciplina disciplina2 = criarDisciplinaUseCase.executar("Disciplina Bloqueada B " + sufixo);
        simulado = criarSimuladoUseCase.executar(
                "Simulado Bloqueado " + sufixo,
                List.of(disciplina1.getId(), disciplina2.getId())
        );
        lancarNotaUseCase.executar(aluno.getId(), simulado.getId(), disciplina1.getId(), 8.0);
    }

    @Dado("que existe um simulado cadastrado")
    public void queExisteUmSimuladoCadastrado() {
        queOCoordenadorDesejaCriarUmSimulado();
        String sufixo = UUID.randomUUID().toString();
        Disciplina disciplina1 = criarDisciplinaUseCase.executar("Disciplina Atualizacao A " + sufixo);
        Disciplina disciplina2 = criarDisciplinaUseCase.executar("Disciplina Atualizacao B " + sufixo);
        simulado = criarSimuladoUseCase.executar(
                "Simulado Atualizacao " + sufixo,
                List.of(disciplina1.getId(), disciplina2.getId())
        );
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

    @Quando("ele tenta alterar as disciplinas do simulado")
    public void eleTentaAlterarAsDisciplinasDoSimulado() {
        try {
            String sufixo = UUID.randomUUID().toString();
            Disciplina novaDisciplina1 = criarDisciplinaUseCase.executar("Nova Disciplina A " + sufixo);
            Disciplina novaDisciplina2 = criarDisciplinaUseCase.executar("Nova Disciplina B " + sufixo);
            simulado = atualizarSimuladoUseCase.executar(
                    simulado.getId(),
                    simulado.getDescricao(),
                    List.of(novaDisciplina1.getId(), novaDisciplina2.getId())
            );
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            excecao = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Quando("ele tenta atualizar o simulado com uma disciplina inativa")
    public void eleTentaAtualizarOSimuladoComUmaDisciplinaInativa() {
        try {
            String sufixo = UUID.randomUUID().toString();
            Disciplina disciplinaInativa = criarDisciplinaUseCase.executar("Disciplina Inativa Atualizacao " + sufixo);
            disciplinaInativa = inativarDisciplinaUseCase.executar(disciplinaInativa.getId());
            Disciplina disciplinaAtiva = criarDisciplinaUseCase.executar("Disciplina Ativa Atualizacao " + sufixo);
            simulado = atualizarSimuladoUseCase.executar(
                    simulado.getId(),
                    simulado.getDescricao(),
                    List.of(disciplinaInativa.getId(), disciplinaAtiva.getId())
            );
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

    @Então("o sistema informa que simulado com notas lançadas não pode ser alterado")
    public void oSistemaInformaQueSimuladoComNotasNaoPodeTerDisciplinasAlteradas() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Simulado com notas lançadas não pode ser alterado", context.getMensagem());
    }

    @Então("o sistema informa que simulado nao pode ser atualizado com disciplina inativa")
    public void oSistemaInformaQueSimuladoNaoPodeSerAtualizadoComDisciplinaInativa() {
        assertFalse(context.isOperacaoExecutada());
        assertNotNull(excecao);
        assertEquals("Disciplina inativa não pode ser vinculada a simulado", context.getMensagem());
    }
}

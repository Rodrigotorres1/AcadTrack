package g8.acadtrack.bdd.steps;

import g8.acadtrack.aplicacao.aluno.CriarAlunoUseCase;
import g8.acadtrack.aplicacao.turma.CriarTurmaUseCase;
import g8.acadtrack.aplicacao.turma.LimparTurmasDuplicadasUseCase;
import g8.acadtrack.aplicacao.turma.VincularAlunoTurmaUseCase;
import g8.acadtrack.bdd.support.TestContext;
import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.turma.Turma;
import g8.acadtrack.dominioacademico.turma.TurmaRepository;
import io.cucumber.java.pt.*;

import static org.junit.jupiter.api.Assertions.*;

public class VincularAlunoTurmaSteps {

    private final TestContext context;
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final CriarTurmaUseCase criarTurmaUseCase;
    private final VincularAlunoTurmaUseCase useCase;
    private final LimparTurmasDuplicadasUseCase limparTurmasDuplicadasUseCase;
    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;

    private Aluno aluno;
    private Aluno alunoTurmaDuplicada;
    private Turma turma;
    private Turma turmaMantida;
    private Turma turmaDuplicada;
    private Exception erroTurma;
    private int turmasRemovidas;

    public VincularAlunoTurmaSteps(
            TestContext context,
            CriarAlunoUseCase criarAlunoUseCase,
            CriarTurmaUseCase criarTurmaUseCase,
            VincularAlunoTurmaUseCase useCase,
            LimparTurmasDuplicadasUseCase limparTurmasDuplicadasUseCase,
            TurmaRepository turmaRepository,
            AlunoRepository alunoRepository
    ) {
        this.context = context;
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.criarTurmaUseCase = criarTurmaUseCase;
        this.useCase = useCase;
        this.limparTurmasDuplicadasUseCase = limparTurmasDuplicadasUseCase;
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Dado("que o aluno {string} não está vinculado a nenhuma turma")
    public void dadoAlunoSemTurma(String nome) {
        aluno = criarAlunoUseCase.executar(nome, emailSeguro(nome) + "@email.com");
        turma = criarTurmaUseCase.executar("Turma A");
    }

    @Dado("que o aluno {string} já está vinculado à turma {string}")
    public void dadoAlunoJaVinculado(String nome, String nomeTurma) {
        aluno = criarAlunoUseCase.executar(nome, emailSeguro(nome) + "@email.com");
        turma = criarTurmaUseCase.executar(nomeTurma);

        useCase.executar(aluno.getId(), turma.getId());
    }

    @Quando("o coordenador vincula o aluno {string} à turma {string}")
    public void quandoVincula(String nome, String turmaNome) {
        try {
            useCase.executar(aluno.getId(), turma.getId());
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema registra o vínculo do aluno {string} à turma {string}")
    public void entaoSucesso(String nome, String turmaNome) {
        assertTrue(context.isOperacaoExecutada());
    }

    @Então("o sistema informa que o aluno já está vinculado a uma turma")
    public void entaoErro() {
        assertFalse(context.isOperacaoExecutada());
        assertEquals("O aluno já está vinculado a uma turma", context.getMensagem());
    }
    @Dado("que já existe uma turma chamada {string}")
    public void dadoQueJaExisteUmaTurmaChamada(String nomeTurma) {
        turma = criarTurmaUseCase.executar(nomeTurma);
        erroTurma = null;
    }

    @Quando("o coordenador tenta criar outra turma chamada {string}")
    public void quandoTentaCriarOutraTurmaChamada(String nomeTurma) {
        try {
            criarTurmaUseCase.executar(nomeTurma);
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            erroTurma = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema informa que a turma já está cadastrada")
    public void entaoSistemaInformaTurmaJaCadastrada() {
        assertNotNull(erroTurma);
        assertFalse(context.isOperacaoExecutada());
        assertEquals("Ja existe uma turma cadastrada com esse nome.", context.getMensagem());
    }

    @Dado("que existem turmas duplicadas {string} e {string} com alunos vinculados")
    public void dadoQueExistemTurmasDuplicadasComAlunosVinculados(String nomeTurmaMantida, String nomeTurmaDuplicada) {
        context.resetMensagens();
        erroTurma = null;
        turmasRemovidas = 0;

        turmaMantida = turmaRepository.salvar(new Turma(null, nomeTurmaMantida));
        turmaDuplicada = turmaRepository.salvar(new Turma(null, nomeTurmaDuplicada));

        aluno = alunoRepository.salvar(new Aluno(null, "Aluno Turma Mantida", "aluno.mantida@email.com", turmaMantida.getId(), null));
        alunoTurmaDuplicada = alunoRepository.salvar(new Aluno(null, "Aluno Turma Duplicada", "aluno.duplicada@email.com", turmaDuplicada.getId(), null));
    }

    @Quando("o coordenador limpa as turmas duplicadas")
    public void quandoOCoordenadorLimpaAsTurmasDuplicadas() {
        try {
            turmasRemovidas = limparTurmasDuplicadasUseCase.executar();
            context.setOperacaoExecutada(true);
        } catch (Exception e) {
            erroTurma = e;
            context.setMensagem(e.getMessage());
            context.setOperacaoExecutada(false);
        }
    }

    @Então("o sistema remove {int} turma duplicada")
    public void entaoOSistemaRemoveTurmaDuplicada(Integer quantidadeEsperada) {
        assertTrue(context.isOperacaoExecutada());
        assertNull(erroTurma);
        assertEquals(quantidadeEsperada, turmasRemovidas);
        assertTrue(turmaRepository.buscarPorId(turmaMantida.getId()).isPresent());
        assertTrue(turmaRepository.buscarPorId(turmaDuplicada.getId()).isEmpty());
    }

    @Então("os alunos da turma duplicada ficam vinculados à turma mantida")
    public void entaoOsAlunosDaTurmaDuplicadaFicamVinculadosATurmaMantida() {
        Aluno alunoMantidoAtualizado = alunoRepository.buscarPorId(aluno.getId())
                .orElseThrow(() -> new AssertionError("Aluno da turma mantida não encontrado"));
        Aluno alunoMigrado = alunoRepository.buscarPorId(alunoTurmaDuplicada.getId())
                .orElseThrow(() -> new AssertionError("Aluno da turma duplicada não encontrado"));

        assertEquals(turmaMantida.getId(), alunoMantidoAtualizado.getTurmaId());
        assertEquals(turmaMantida.getId(), alunoMigrado.getTurmaId());
    }

    private String emailSeguro(String nome) {
        return nome.toLowerCase().replace(" ", ".");
    }
}

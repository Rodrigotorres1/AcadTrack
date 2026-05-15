package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.aluno.Aluno;
import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidacaoLancamentoNotaService {

    private final ValidadorLancamentoNota cadeiaValidacao;

    public ValidacaoLancamentoNotaService(
            AlunoRepository alunoRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            NotaRepository notaRepository
    ) {
        this.cadeiaValidacao = new ValidadorValorNotaDecorator(
                new ValidadorEntidadesLancamentoNotaDecorator(
                        new ValidadorAlunoAtivoDecorator(
                                new ValidadorDisciplinaAtivaDecorator(
                                        new ValidadorDisciplinaVinculadaSimuladoDecorator(
                                                new ValidadorNotaDuplicadaDecorator(
                                                        new ValidadorLancamentoNotaBase(),
                                                        notaRepository
                                                )
                                        ),
                                        disciplinaRepository
                                )
                        ),
                        alunoRepository,
                        simuladoRepository,
                        disciplinaRepository
                )
        );
    }

    public Aluno validar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        DadosLancamentoNota dados = new DadosLancamentoNota(alunoId, simuladoId, disciplinaId, valor);
        cadeiaValidacao.validar(dados);
        return dados.aluno();
    }
}

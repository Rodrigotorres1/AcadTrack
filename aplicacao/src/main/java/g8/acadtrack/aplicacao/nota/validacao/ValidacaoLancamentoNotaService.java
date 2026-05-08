package g8.acadtrack.aplicacao.nota.validacao;

import g8.acadtrack.dominioacademico.aluno.AlunoRepository;
import g8.acadtrack.dominioacademico.disciplina.DisciplinaRepository;
import g8.acadtrack.dominioavaliacao.nota.NotaRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoRepository;
import g8.acadtrack.dominioavaliacao.simulado.SimuladoDisciplinaRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidacaoLancamentoNotaService {

    private final ValidadorLancamentoNota cadeiaValidacao;

    public ValidacaoLancamentoNotaService(
            AlunoRepository alunoRepository,
            SimuladoRepository simuladoRepository,
            DisciplinaRepository disciplinaRepository,
            SimuladoDisciplinaRepository simuladoDisciplinaRepository,
            NotaRepository notaRepository
    ) {
        this.cadeiaValidacao = new ValidadorValorNotaDecorator(
                new ValidadorEntidadesLancamentoNotaDecorator(
                        new ValidadorDisciplinaVinculadaSimuladoDecorator(
                                new ValidadorDisciplinaAtivaDecorator(
                                        new ValidadorNotaDuplicadaDecorator(
                                                new ValidadorLancamentoNotaBase(),
                                                notaRepository
                                        ),
                                        disciplinaRepository
                                ),
                                simuladoDisciplinaRepository
                        ),
                        alunoRepository,
                        simuladoRepository,
                        disciplinaRepository
                )
        );
    }

    public void validar(Long alunoId, Long simuladoId, Long disciplinaId, double valor) {
        cadeiaValidacao.validar(new DadosLancamentoNota(alunoId, simuladoId, disciplinaId, valor));
    }
}

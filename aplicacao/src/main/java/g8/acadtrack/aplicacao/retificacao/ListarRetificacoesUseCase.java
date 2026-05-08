package g8.acadtrack.aplicacao.retificacao;

import g8.acadtrack.dominioavaliacao.retificacao.SolicitacaoRetificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarRetificacoesUseCase {

    private final SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository;
    private final MontarDetalheRetificacaoService montarDetalheRetificacaoService;

    public ListarRetificacoesUseCase(
            SolicitacaoRetificacaoRepository solicitacaoRetificacaoRepository,
            MontarDetalheRetificacaoService montarDetalheRetificacaoService
    ) {
        this.solicitacaoRetificacaoRepository = solicitacaoRetificacaoRepository;
        this.montarDetalheRetificacaoService = montarDetalheRetificacaoService;
    }

    public List<SolicitacaoRetificacaoDetalheResultado> executar() {
        return solicitacaoRetificacaoRepository.buscarTodas()
                .stream()
                .map(montarDetalheRetificacaoService::montar)
                .toList();
    }
}

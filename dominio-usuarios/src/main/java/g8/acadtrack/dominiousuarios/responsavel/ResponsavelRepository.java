package g8.acadtrack.dominiousuarios.responsavel;

import java.util.List;
import java.util.Optional;

public interface ResponsavelRepository {

    Responsavel salvar(Responsavel responsavel);

    List<Responsavel> buscarTodos();

    Optional<Responsavel> buscarPorId(Long id);
}

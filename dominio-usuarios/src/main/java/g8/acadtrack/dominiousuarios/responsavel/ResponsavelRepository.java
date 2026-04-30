package g8.acadtrack.dominiousuarios.responsavel;

import java.util.Optional;

public interface ResponsavelRepository {

    Responsavel salvar(Responsavel responsavel);

    Optional<Responsavel> buscarPorId(Long id);
}
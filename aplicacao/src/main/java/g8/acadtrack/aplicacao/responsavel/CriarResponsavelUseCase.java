package g8.acadtrack.aplicacao.responsavel;

import g8.acadtrack.dominiocompartilhado.excecao.ConflitoDeEstadoException;
import g8.acadtrack.dominiocompartilhado.email.Email;
import g8.acadtrack.dominiousuarios.responsavel.Responsavel;
import g8.acadtrack.dominiousuarios.responsavel.ResponsavelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarResponsavelUseCase {

    private final ResponsavelRepository responsavelRepository;

    public CriarResponsavelUseCase(ResponsavelRepository responsavelRepository) {
        this.responsavelRepository = responsavelRepository;
    }

    @Transactional
    public Responsavel executar(String nome, String email) {
        String emailNormalizado = Email.normalizar(email);
        if (responsavelRepository.existeResponsavelComEmailIgnorandoMaiusculas(emailNormalizado)) {
            throw new ConflitoDeEstadoException("Já existe responsável cadastrado com este e-mail");
        }
        Responsavel responsavel = new Responsavel(null, nome, emailNormalizado);
        return responsavelRepository.salvar(responsavel);
    }
}

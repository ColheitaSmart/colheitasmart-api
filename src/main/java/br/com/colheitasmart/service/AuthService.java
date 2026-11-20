
package br.com.colheitasmart.service;

import br.com.colheitasmart.dao.AgricultorDAO;
import br.com.colheitasmart.model.Agricultor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    private final AgricultorDAO agricultorDAO;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AgricultorDAO agricultorDAO,
            PasswordEncoder passwordEncoder) {

        this.agricultorDAO = agricultorDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Agricultor cadastrar(
            String nome,
            String email,
            String senha) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                "Informe seu nome."
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                "Informe seu e-mail."
            );
        }

        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException(
                "A senha deve ter pelo menos 8 caracteres."
            );
        }

        String emailNormalizado =
            email.trim().toLowerCase(Locale.ROOT);

        if (agricultorDAO.buscarPorEmail(emailNormalizado).isPresent()) {
            throw new IllegalArgumentException(
                "Este e-mail ja esta cadastrado."
            );
        }

        Agricultor agricultor = new Agricultor();

        agricultor.setNmAgricultor(nome.trim());
        agricultor.setDsEmail(emailNormalizado);

        // Campos obrigatorios no banco atual.
        // Valores temporarios ate ampliarmos o formulario.
        agricultor.setNrTelefone("Nao informado");
        agricultor.setNmCidade("Nao informada");

        agricultor.setDsSenhaHash(
            passwordEncoder.encode(senha)
        );

        return agricultorDAO.cadastrar(agricultor);
    }

    public Optional<Agricultor> autenticar(
            String email,
            String senha) {

        if (email == null || senha == null) {
            return Optional.empty();
        }

        String emailNormalizado =
            email.trim().toLowerCase(Locale.ROOT);

        Optional<Agricultor> encontrado =
            agricultorDAO.buscarPorEmail(emailNormalizado);

        if (encontrado.isEmpty()) {
            return Optional.empty();
        }

        Agricultor agricultor = encontrado.get();

        String hash = agricultor.getDsSenhaHash();

        if (hash == null ||
            !passwordEncoder.matches(senha, hash)) {

            return Optional.empty();
        }

        return Optional.of(agricultor);
    }
}


package br.com.colheitasmart.controller;

import br.com.colheitasmart.model.Agricultor;
import br.com.colheitasmart.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public record CadastroRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String senha
    ) {}

    public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha
    ) {}

    public record AgricultorResponse(
        Long id,
        String nome,
        String email
    ) {}

    private AgricultorResponse resposta(Agricultor agricultor) {
        return new AgricultorResponse(
            agricultor.getCdAgricultor(),
            agricultor.getNmAgricultor(),
            agricultor.getDsEmail()
        );
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(
            @Valid @RequestBody CadastroRequest dados) {

        try {
            Agricultor agricultor = authService.cadastrar(
                dados.nome(),
                dados.email(),
                dados.senha()
            );

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta(agricultor));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("erro", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest dados,
            HttpServletRequest request,
            HttpServletResponse response) {

        var encontrado = authService.autenticar(
            dados.email(),
            dados.senha()
        );

        if (encontrado.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("erro", "E-mail ou senha invalidos."));
        }

        Agricultor agricultor = encontrado.get();

        // Evita reutilizar uma sessao anterior.
        HttpSession sessaoAnterior = request.getSession(false);

        if (sessaoAnterior != null) {
            sessaoAnterior.invalidate();
        }

        // Identidade autenticada do agricultor.
        Authentication autenticacao =
            new UsernamePasswordAuthenticationToken(
                agricultor.getCdAgricultor().toString(),
                null,
                java.util.List.of(
                    new org.springframework.security.core.authority
                        .SimpleGrantedAuthority("ROLE_AGRICULTOR")
                )
            );

        SecurityContext contexto =
            SecurityContextHolder.createEmptyContext();

        contexto.setAuthentication(autenticacao);
        SecurityContextHolder.setContext(contexto);

        // Persiste a autenticacao na sessao HTTP.
        new HttpSessionSecurityContextRepository()
            .saveContext(contexto, request, response);

        return ResponseEntity.ok(resposta(agricultor));
    }

    @GetMapping("/me")
    public ResponseEntity<?> usuarioAtual(
            Authentication autenticacao) {

        if (autenticacao == null ||
            !autenticacao.isAuthenticated()) {

            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("erro", "Nao autenticado."));
        }

        return ResponseEntity.ok(
            Map.of(
                "id", Long.valueOf(autenticacao.getName()),
                "autenticado", true
            )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request) {

        HttpSession sessao = request.getSession(false);

        if (sessao != null) {
            sessao.invalidate();
        }

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(
            Map.of("mensagem", "Sessao encerrada.")
        );
    }
}

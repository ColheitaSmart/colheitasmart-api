package br.com.colheitasmart.handler;

import br.com.colheitasmart.dto.ErroDTO;
import br.com.colheitasmart.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.LinkedHashMap;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private ResponseEntity<ErroDTO> erro(int status, String mensagem, HttpServletRequest req, Map<String, String> campos) {
        return ResponseEntity.status(status).body(new ErroDTO(Instant.now(), status, mensagem, req.getRequestURI(), campos));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> validacao(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return erro(400, "Confira os campos informados.", req, campos);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class})
    public ResponseEntity<ErroDTO> requisicao(Exception ex, HttpServletRequest req) {
        return erro(400, "JSON, tipo ou parametro invalido.", req, Map.of());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> ausente(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        return erro(404, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroDTO> rota(NoResourceFoundException ex, HttpServletRequest req) {
        return erro(404, "Rota nao encontrada.", req, Map.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroDTO> regra(RegraNegocioException ex, HttpServletRequest req) {
        return erro(409, ex.getMessage(), req, Map.of());
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErroDTO> banco(PersistenciaException ex, HttpServletRequest req) {
        if (ex.conflito()) return erro(409, "Registro duplicado, vinculo invalido ou registro com dependentes.", req, Map.of());
        log.error("Falha de persistencia em {}", req.getRequestURI(), ex);
        return erro(500, "Nao foi possivel concluir a operacao.", req, Map.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroDTO> metodo(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        ResponseEntity<ErroDTO> resposta = erro(405, "Metodo HTTP nao permitido nesta rota.", req, Map.of());
        HttpHeaders headers = new HttpHeaders();
        if (ex.getSupportedHttpMethods() != null) headers.setAllow(ex.getSupportedHttpMethods());
        return new ResponseEntity<>(resposta.getBody(), headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroDTO> formato(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return erro(415, "Use Content-Type application/json.", req, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> inesperado(Exception ex, HttpServletRequest req) {
        log.error("Erro inesperado em {}", req.getRequestURI(), ex);
        return erro(500, "Erro interno. Tente novamente mais tarde.", req, Map.of());
    }
}

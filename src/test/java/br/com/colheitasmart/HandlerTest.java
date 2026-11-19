package br.com.colheitasmart;
import br.com.colheitasmart.exception.PersistenciaException;
import br.com.colheitasmart.handler.ApiExceptionHandler;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;

class HandlerTest {
    @Test void falhaDeBancoNaoExpoeDetalhes() {
        var handler = new ApiExceptionHandler();
        var resposta = handler.banco(new PersistenciaException(new SQLException("SELECT segredo FROM tabela")), new MockHttpServletRequest());
        assertEquals(500, resposta.getStatusCode().value());
        assertNotNull(resposta.getBody());
        assertFalse(resposta.getBody().mensagem().contains("SELECT"));
        assertTrue(resposta.getBody().campos().isEmpty());
    }
    @Test void falhaInesperadaNaoExpoeDetalhes() {
        var resposta = new ApiExceptionHandler().inesperado(new RuntimeException("detalhe interno"), new MockHttpServletRequest());
        assertEquals(500, resposta.getStatusCode().value());
        assertFalse(resposta.getBody().mensagem().contains("detalhe interno"));
    }
}

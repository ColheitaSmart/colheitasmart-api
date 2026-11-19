package br.com.colheitasmart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = {"/reset.sql", "/estrutura.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    String agricultor = """
        {"nmAgricultor":"Ana","dsEmail":"ana@example.com","nrTelefone":"11999999999","nmCidade":"Sao Paulo"}
        """;
    String cultura = """
        {"nmCultura":"Tomate","dsCategoria":"Hortalica"}
        """;
    long criar(String rota, String corpo, String chave) throws Exception {
        String resposta = mvc.perform(post(rota).contentType("application/json").content(corpo))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andReturn().getResponse().getContentAsString();
        return json.readTree(resposta).get(chave).asLong();
    }
    String lote(long a, long c, String inicial, String disponivel) {
        return """
            {"cdAgricultor":%d,"cdCultura":%d,"qtPesoInicialKg":%s,"qtPesoDisponivelKg":%s,
            "dtColheita":"2026-01-01","dsArmazenamento":"Galpao","vlPrecoKg":4.50,"dsStatus":"DISPONIVEL"}
            """.formatted(a,c,inicial,disponivel);
    }
    @Test void crudAgricultor() throws Exception {
        long id = criar("/agricultores", agricultor, "cdAgricultor");
        mvc.perform(get("/agricultores")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
        mvc.perform(get("/agricultores/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.nmAgricultor").value("Ana"));
        mvc.perform(put("/agricultores/"+id).contentType("application/json").content(agricultor.replace("Ana","Bia")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nmAgricultor").value("Bia"));
        mvc.perform(delete("/agricultores/"+id)).andExpect(status().isNoContent());
        mvc.perform(get("/agricultores/"+id)).andExpect(status().isNotFound());
    }
    @Test void crudCultura() throws Exception {
        long id = criar("/culturas", cultura, "cdCultura");
        mvc.perform(get("/culturas")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
        mvc.perform(get("/culturas/"+id)).andExpect(status().isOk());
        mvc.perform(put("/culturas/"+id).contentType("application/json").content(cultura.replace("Tomate","Batata")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nmCultura").value("Batata"));
        mvc.perform(delete("/culturas/"+id)).andExpect(status().isNoContent());
    }
    @Test void crudLoteEProtecaoDosPais() throws Exception {
        long a=criar("/agricultores",agricultor,"cdAgricultor"), c=criar("/culturas",cultura,"cdCultura");
        long l=criar("/agricultores/"+a+"/lotes",lote(a,c,"200","200"),"cdLote");
        mvc.perform(get("/lotes")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
        mvc.perform(get("/lotes/"+l)).andExpect(status().isOk()).andExpect(jsonPath("$.cdAgricultor").value(a));
        mvc.perform(get("/agricultores/"+a+"/lotes")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
        mvc.perform(get("/culturas/"+c+"/lotes")).andExpect(status().isOk()).andExpect(jsonPath("$",hasSize(1)));
        mvc.perform(put("/lotes/"+l).contentType("application/json").content(lote(a,c,"200","150")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.qtPesoDisponivelKg").value(150));
        mvc.perform(delete("/agricultores/"+a)).andExpect(status().isConflict());
        mvc.perform(delete("/culturas/"+c)).andExpect(status().isConflict());
        mvc.perform(delete("/lotes/"+l)).andExpect(status().isNoContent());
        mvc.perform(delete("/culturas/"+c)).andExpect(status().isNoContent());
        mvc.perform(delete("/agricultores/"+a)).andExpect(status().isNoContent());
    }
    @Test void pesoAcimaDoInicial() throws Exception {
        long a=criar("/agricultores",agricultor,"cdAgricultor"), c=criar("/culturas",cultura,"cdCultura");
        mvc.perform(post("/lotes").contentType("application/json").content(lote(a,c,"10","11")))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
    }
    @Test void entradaInvalida() throws Exception {
        mvc.perform(post("/agricultores").contentType("application/json").content("{}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.campos.nmAgricultor").exists());
        mvc.perform(post("/culturas").contentType("application/json").content(cultura.replace("Tomate","")))
            .andExpect(status().isBadRequest());
        mvc.perform(post("/lotes").contentType("application/json").content(lote(1,1,"0","0")))
            .andExpect(status().isBadRequest());
    }
    @Test void emailDuplicado() throws Exception {
        criar("/agricultores",agricultor,"cdAgricultor");
        mvc.perform(post("/agricultores").contentType("application/json").content(agricultor))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.mensagem",not(containsString("INSERT"))));
    }
    @Test void vinculoAusente() throws Exception {
        mvc.perform(post("/lotes").contentType("application/json").content(lote(999,999,"10","10")))
            .andExpect(status().isNotFound());
        mvc.perform(get("/agricultores/999/lotes")).andExpect(status().isNotFound());
    }
    @Test void criacaoAninhadaDeCulturaEConflito() throws Exception {
        long a=criar("/agricultores",agricultor,"cdAgricultor"), c=criar("/culturas",cultura,"cdCultura");
        criar("/culturas/"+c+"/lotes",lote(a,c,"10","10"),"cdLote");
        mvc.perform(post("/agricultores/"+a+"/lotes").contentType("application/json").content(lote(a+1,c,"10","10")))
            .andExpect(status().isConflict());
    }
    @Test void errosDoContrato() throws Exception {
        mvc.perform(get("/lotes/999")).andExpect(status().isNotFound());
        mvc.perform(put("/culturas/999").contentType("application/json").content(cultura)).andExpect(status().isNotFound());
        mvc.perform(delete("/lotes/999")).andExpect(status().isNotFound());
        mvc.perform(get("/lotes/abc")).andExpect(status().isBadRequest());
        mvc.perform(post("/culturas").contentType("application/json").content("{")).andExpect(status().isBadRequest());
        mvc.perform(patch("/culturas")).andExpect(status().isMethodNotAllowed()).andExpect(header().exists("Allow"));
        mvc.perform(get("/rota-inexistente")).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith("application/json"));
    }
    @Test void cors() throws Exception {
        mvc.perform(options("/lotes").header("Origin","http://localhost:5173")
            .header("Access-Control-Request-Method","POST"))
            .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin","http://localhost:5173"));
        mvc.perform(options("/lotes").header("Origin","https://outro.example")
            .header("Access-Control-Request-Method","POST")).andExpect(status().isForbidden());
    }
}

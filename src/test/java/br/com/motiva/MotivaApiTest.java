package br.com.motiva;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de API (Aula 15): sobe a aplicação inteira com @SpringBootTest,
 * usando H2 em memória (perfil "test") no lugar do Oracle.
 */
@SpringBootTest
@ActiveProfiles("test")
class MotivaApiTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // ------------------------------------------------------------ CRUD Trecho

    @Test
    void deveCriarTrechoERetornar201ComClassificacao() throws Exception {
        mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTrecho("BR-116", 10, 15, 50, "UMIDO", true)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.monitoradoIot").value(true))
                .andExpect(jsonPath("$.classificacao").value("URGENTE"));
    }

    @Test
    void deveRetornar404ParaTrechoInexistente() throws Exception {
        mockMvc.perform(get("/api/trechos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem", notNullValue()));
    }

    @Test
    void deveRetornar400QuandoNivelVegetacaoNegativo() throws Exception {
        mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTrecho("BR-101", 1, 2, -5, "SECO", false)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nivelVegetacao", notNullValue()));
    }

    @Test
    void deveRetornar400QuandoKmFinalNaoForMaiorQueInicial() throws Exception {
        mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTrecho("BR-101", 20, 10, 5, "SECO", false)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarERemoverTrecho() throws Exception {
        long id = criarTrecho("BR-050", 5, 10, 10, "SECO", false);

        mockMvc.perform(put("/api/trechos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTrecho("BR-050", 5, 10, 35, "SECO", false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nivelVegetacao").value(35.0))
                .andExpect(jsonPath("$.classificacao").value("CRITICO"));

        mockMvc.perform(delete("/api/trechos/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/trechos/" + id)).andExpect(status().isNotFound());
    }

    // ------------------------------------------------- Intervenção + Equipe

    @Test
    void motorDeRegrasDeveDecidirTipoDaIntervencaoEBloquearExclusaoDoTrecho() throws Exception {
        long trechoId = criarTrecho("SP-330", 100, 110, 48, "UMIDO", false);
        long equipeId = criarEquipe("Equipe Teste", "Maria Souza");

        mockMvc.perform(post("/api/intervencoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"trechoId\":" + trechoId + ",\"equipeId\":" + equipeId
                                + ",\"observacao\":\"Teste automatizado\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("ROCADA_MECANIZADA"))
                .andExpect(jsonPath("$.urgente").value(true))
                .andExpect(jsonPath("$.equipeId").value(equipeId));

        mockMvc.perform(get("/api/equipes/" + equipeId + "/intervencoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));

        // Trecho com intervenção vinculada não pode ser excluído
        mockMvc.perform(delete("/api/trechos/" + trechoId)).andExpect(status().isConflict());
    }

    // ------------------------------------------------------------- Relatório

    @Test
    void deveGerarRelatorioEConsultarPorPeriodo() throws Exception {
        criarTrecho("BR-381", 0, 5, 20, "SECO", false);

        mockMvc.perform(post("/api/relatorios"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.relatorio.id", notNullValue()))
                .andExpect(jsonPath("$.itens.length()", greaterThanOrEqualTo(1)));

        String hoje = LocalDate.now().toString();
        mockMvc.perform(get("/api/relatorios/periodo").param("inicio", hoje).param("fim", hoje))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));

        mockMvc.perform(get("/api/relatorios/periodo").param("inicio", "2026-12-31").param("fim", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------- Auxiliares

    private long criarTrecho(String rodovia, double kmIni, double kmFim, double nivel,
                             String terreno, boolean iot) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTrecho(rodovia, kmIni, kmFim, nivel, terreno, iot)))
                .andExpect(status().isCreated())
                .andReturn();
        return extrairId(resultado);
    }

    private long criarEquipe(String nome, String responsavel) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeEquipe\":\"" + nome + "\",\"responsavel\":\"" + responsavel + "\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        return extrairId(resultado);
    }

    private long extrairId(MvcResult resultado) throws Exception {
        Number id = JsonPath.read(resultado.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }

    private String jsonTrecho(String rodovia, double kmIni, double kmFim, double nivel,
                              String terreno, boolean iot) {
        return "{\"nomeRodovia\":\"" + rodovia + "\","
                + "\"quilometroInicial\":" + kmIni + ","
                + "\"quilometroFinal\":" + kmFim + ","
                + "\"nivelVegetacao\":" + nivel + ","
                + "\"tipoTerreno\":\"" + terreno + "\","
                + "\"monitoradoIot\":" + iot + "}";
    }
}

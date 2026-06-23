package com.enterprise.gustadev.fintech_app.adapters.in.web.banco;

import com.enterprise.gustadev.fintech_app.application.banco.usecase.BuscarBancoUseCase;
import com.enterprise.gustadev.fintech_app.application.banco.usecase.CriarBancoUseCase;
import com.enterprise.gustadev.fintech_app.application.banco.usecase.ListarBancosUseCase;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BancoControllerTest {

    @Mock
    private CriarBancoUseCase criarUseCase;

    @Mock
    private ListarBancosUseCase listarUseCase;

    @Mock
    private BuscarBancoUseCase buscarUseCase;

    @InjectMocks
    private BancoController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Banco bancoCompleto(Long id, String code, String nome) {
        return new Banco(id, code, nome, "Banco de teste", "#000000", "icon");
    }

    @Test
    void criar_deveRetornar201ComLocationHeader_quandoDadosValidos() throws Exception {
        Banco salvo = bancoCompleto(1L, "NUBANK", "Nubank");
        when(criarUseCase.executar(any())).thenReturn(salvo);

        mockMvc.perform(post("/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Nubank",
                                  "descricao": "Banco digital",
                                  "corHex": "#8A05BE",
                                  "icone": "nubank-icon"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/bancos/1/NUBANK"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("NUBANK"))
                .andExpect(jsonPath("$.nome").value("Nubank"));
    }

    @Test
    void criar_deveRetornar400_quandoNomeAusente() throws Exception {
        mockMvc.perform(post("/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Sem nome",
                                  "corHex": "#000000",
                                  "icone": "icon"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_deveRetornar200ComTodosOsBancos() throws Exception {
        when(listarUseCase.executar()).thenReturn(List.of(
                bancoCompleto(1L, "NUBANK", "Nubank"),
                bancoCompleto(2L, "ITAU01", "Itaú")
        ));

        mockMvc.perform(get("/bancos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("NUBANK"))
                .andExpect(jsonPath("$[1].code").value("ITAU01"));
    }

    @Test
    void buscarPorId_deveRetornar200ComBanco() throws Exception {
        when(buscarUseCase.executar(any(), anyString()))
                .thenReturn(bancoCompleto(1L, "NUBANK", "Nubank"));

        mockMvc.perform(get("/bancos/{banco_id}/{banco_code}", 1L, "NUBANK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("NUBANK"));
    }
}

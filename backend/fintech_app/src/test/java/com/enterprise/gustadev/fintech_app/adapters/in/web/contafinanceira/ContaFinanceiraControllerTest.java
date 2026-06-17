package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira;

import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.RemoverContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.ListarContasFinanceirasUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContaFinanceiraControllerTest {

    @Mock
    private CriarContaFinanceiraUseCase criarUseCase;

    @Mock
    private ListarContasFinanceirasUseCase listarUseCase;

    @Mock
    private BuscarContaFinanceiraUseCase buscarUseCase;

    @Mock
    private RemoverContaFinanceiraUseCase removerUseCase;

    @InjectMocks
    private ContaFinanceiraController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private ContaFinanceira contaCompleta(Long id, Long usuarioId, Long bancoId, String bancoCode) {
        return new ContaFinanceira(id, usuarioId, TipoConta.corrente,
                bancoId, bancoCode, new BigDecimal("1000.00"), true, true,
                OffsetDateTime.now(), null);
    }

    @Test
    void criar_deveRetornar201_eVincularUsuarioAoBanco_quandoDadosValidos() throws Exception {
        Long usuarioId = 1L;
        Long bancoId = 10L;
        String bancoCode = "NUBANK";
        when(criarUseCase.executar(any())).thenReturn(contaCompleta(1L, usuarioId, bancoId, bancoCode));

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": %d,
                                  "tipo": "corrente",
                                  "bancoId": %d,
                                  "bancoCode": "%s",
                                  "saldoInicial": 1000.00,
                                  "padrao": true
                                }
                                """.formatted(usuarioId, bancoId, bancoCode)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuarioId").value(usuarioId.intValue()))
                .andExpect(jsonPath("$.bancoId").value(bancoId.intValue()))
                .andExpect(jsonPath("$.bancoCode").value(bancoCode))
                .andExpect(jsonPath("$.tipo").value("corrente"));
    }

    @Test
    void criar_deveRetornar400_quandoBancoIdAusente() throws Exception {
        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": 1,
                                  "tipo": "corrente",
                                  "bancoCode": "NUBANK",
                                  "saldoInicial": 1000.00,
                                  "padrao": true
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPorUsuario_deveRetornar200ComContasDeMultiplosBancos() throws Exception {
        Long usuarioId = 1L;
        when(listarUseCase.executar(usuarioId))
                .thenReturn(List.of(
                        contaCompleta(1L, usuarioId, 10L, "NUBANK"),
                        contaCompleta(2L, usuarioId, 20L, "ITAU01")
                ));

        mockMvc.perform(get("/contas/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bancoCode").value("NUBANK"))
                .andExpect(jsonPath("$[1].bancoCode").value("ITAU01"));
    }

    @Test
    void buscarPorId_deveRetornar200ComConta() throws Exception {
        Long id = 1L;
        when(buscarUseCase.executar(any(), anyString()))
                .thenReturn(contaCompleta(id, 1L, 10L, "NUBANK"));

        mockMvc.perform(get("/contas/{id_contas}/{contas_code}", id, "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.bancoCode").value("NUBANK"));
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        Long id = 1L;
        doNothing().when(deletarUseCase).executar(any(), anyString());

        mockMvc.perform(delete("/contas/{id_contas}/{contas_code}", id, "ABC123"))
                .andExpect(status().isNoContent());
    }
}

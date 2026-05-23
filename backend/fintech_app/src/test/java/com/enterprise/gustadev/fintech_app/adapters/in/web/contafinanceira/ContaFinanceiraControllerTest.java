package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira;

import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.DeletarContaFinanceiraUseCase;
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
import java.util.UUID;

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
    private DeletarContaFinanceiraUseCase deletarUseCase;

    @InjectMocks
    private ContaFinanceiraController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private ContaFinanceira contaCompleta(UUID id, UUID usuarioId) {
        return new ContaFinanceira(id, usuarioId, "Nubank", TipoConta.corrente,
                "Nubank", new BigDecimal("1000.00"), true, true,
                OffsetDateTime.now(), null);
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        UUID contaId = UUID.randomUUID();
        when(criarUseCase.executar(any())).thenReturn(contaCompleta(contaId, usuarioId));

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": "%s",
                                  "nome": "Nubank",
                                  "tipo": "corrente",
                                  "banco": "Nubank",
                                  "saldoInicial": 1000.00,
                                  "padrao": true
                                }
                                """.formatted(usuarioId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Nubank"))
                .andExpect(jsonPath("$.tipo").value("corrente"));
    }

    @Test
    void listarPorUsuario_deveRetornar200ComLista() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        when(listarUseCase.executar(usuarioId))
                .thenReturn(List.of(contaCompleta(UUID.randomUUID(), usuarioId)));

        mockMvc.perform(get("/contas/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_deveRetornar200ComConta() throws Exception {
        UUID id = UUID.randomUUID();
        when(buscarUseCase.executar(any(), anyString())).thenReturn(contaCompleta(id, UUID.randomUUID()));

        mockMvc.perform(get("/contas/{id_contas}/{contas_code}", id, "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(deletarUseCase).executar(any(), anyString());

        mockMvc.perform(delete("/contas/{id_contas}/{contas_code}", id, "ABC123"))
                .andExpect(status().isNoContent());
    }
}

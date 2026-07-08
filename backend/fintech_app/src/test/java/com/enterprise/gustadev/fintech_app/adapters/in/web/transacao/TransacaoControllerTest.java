package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao;

import com.enterprise.gustadev.fintech_app.application.transacao.usecase.BuscarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.EstornarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ListarTransacoesUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransacaoControllerTest {

    @Mock
    private CriarTransacaoUseCase criarUseCase;

    @Mock
    private ListarTransacoesUseCase listarUseCase;

    @Mock
    private BuscarTransacaoUseCase buscarUseCase;

    @Mock
    private EstornarTransacaoUseCase estornarUseCase;

    @InjectMocks
    private TransacaoController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Transacao transacaoCompleta(Long id, Long contaId) {
        return new Transacao(id, new ContaFinanceira(contaId, "C1"), "N",
                TipoTransacao.GASTO, null, new BigDecimal("150.00"),
                LocalDate.now(), 1L, "CAT001", null, OrigemTransacao.manual,
                StatusRevisaoTransacao.EXTRAIDA, null, false,
                null, null, 1, null, null, null);
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        Long contaId = 1L;
        Long transacaoId = 1L;
        when(criarUseCase.executar(any()))
                .thenReturn(transacaoCompleta(transacaoId, contaId));

        mockMvc.perform(post("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "contaId": "%s",
                                  "contaCode": "C1",
                                  "tipo": "GASTO",
                                  "valor": 150.00,
                                  "dataTransacao": "%s",
                                  "categoriaId": 1,
                                  "categoriaCode": "CAT001",
                                  "origem": "manual"
                                }
                                """.formatted(contaId, LocalDate.now())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.tipo").value("GASTO"));
    }

    @Test
    void listarPorUsuario_deveRetornar200ComLista() throws Exception {
        Long usuarioId = 1L;
        when(listarUseCase.executarPorUsuario(usuarioId))
                .thenReturn(List.of(transacaoCompleta(1L, 1L)));

        mockMvc.perform(get("/transacoes/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarPorConta_deveRetornar200ComLista() throws Exception {
        Long contaId = 1L;
        when(listarUseCase.executarPorConta(contaId))
                .thenReturn(List.of(transacaoCompleta(1L, contaId)));

        mockMvc.perform(get("/transacoes/conta/{contaId}", contaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_deveRetornar200ComTransacao() throws Exception {
        Long id = 1L;
        when(buscarUseCase.executar(any(), anyString()))
                .thenReturn(transacaoCompleta(id, 1L));

        mockMvc.perform(get("/transacoes/{id_transacoes}/{transacoes_code}", id, "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.intValue()));
    }

    @Test
    void estornar_deveRetornar200() throws Exception {
        Long id = 1L;
        when(estornarUseCase.executar(any(), anyString(), any(), anyString()))
                .thenReturn(transacaoCompleta(id, 1L));

        mockMvc.perform(patch("/transacoes/{id_transacoes}/{transacoes_code}/estornar", id, "ABC123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "contaId": 1, "contaCode": "C1" }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.intValue()));
    }
}

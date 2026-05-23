package com.enterprise.gustadev.fintech_app.adapters.in.web.categoria;

import com.enterprise.gustadev.fintech_app.application.categoria.usecase.BuscarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.CriarCategoriaUseCase;
import com.enterprise.gustadev.fintech_app.application.categoria.usecase.ListarCategoriasUseCase;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    @Mock
    private CriarCategoriaUseCase criarUseCase;

    @Mock
    private ListarCategoriasUseCase listarUseCase;

    @Mock
    private BuscarCategoriaUseCase buscarUseCase;

    @InjectMocks
    private CategoriaController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Categoria categoriaCompleta(UUID id) {
        return new Categoria(id, "Alimentação", null, TipoCategoria.gasto, "🍔", "#FF5733", true, null, null);
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        UUID categoriaId = UUID.randomUUID();
        Categoria salva = new Categoria(categoriaId, "Pets", null, TipoCategoria.gasto, "🐶", "#FF0000", false, usuarioId, null);
        when(criarUseCase.executar(any())).thenReturn(salva);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": "%s",
                                  "nome": "Pets",
                                  "tipo": "gasto",
                                  "icone": "🐶",
                                  "corHex": "#FF0000"
                                }
                                """.formatted(usuarioId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Pets"))
                .andExpect(jsonPath("$.tipo").value("gasto"));
    }

    @Test
    void listarPadrao_deveRetornar200ComListaDeCategoriasPadrao() throws Exception {
        when(listarUseCase.executarPadrao()).thenReturn(List.of(categoriaCompleta(UUID.randomUUID())));

        mockMvc.perform(get("/categorias/padrao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].padrao").value(true));
    }

    @Test
    void listarPorUsuario_deveRetornar200ComLista() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        Categoria cat = new Categoria(UUID.randomUUID(), "Viagens", null, TipoCategoria.gasto, "✈", "#0000FF", false, usuarioId, null);
        when(listarUseCase.executarPorUsuario(usuarioId)).thenReturn(List.of(cat));

        mockMvc.perform(get("/categorias/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_deveRetornar200ComCategoria() throws Exception {
        UUID id = UUID.randomUUID();
        when(buscarUseCase.executar(id)).thenReturn(categoriaCompleta(id));

        mockMvc.perform(get("/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }
}

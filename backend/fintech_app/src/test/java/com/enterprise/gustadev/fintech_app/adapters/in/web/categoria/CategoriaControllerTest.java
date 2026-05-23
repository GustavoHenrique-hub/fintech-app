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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private Categoria categoriaCompleta(Long id) {
        return new Categoria(id, "Alimentação", TipoCategoria.gasto, "🍔", "#FF5733", true, null);
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        Long categoriaId = 1L;
        Categoria salva = new Categoria(categoriaId, "Pets", TipoCategoria.gasto, "🐶", "#FF0000", false, null);
        when(criarUseCase.executar(any())).thenReturn(salva);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Pets",
                                  "tipo": "gasto",
                                  "icone": "🐶",
                                  "corHex": "#FF0000"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Pets"))
                .andExpect(jsonPath("$.tipo").value("gasto"));
    }

    @Test
    void listarPadrao_deveRetornar200ComListaDeCategoriasPadrao() throws Exception {
        when(listarUseCase.executarPadrao()).thenReturn(List.of(categoriaCompleta(1L)));

        mockMvc.perform(get("/categorias/padrao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].padrao").value(true));
    }

    @Test
    void buscarPorId_deveRetornar200ComCategoria() throws Exception {
        Long id = 1L;
        when(buscarUseCase.executar(any(), anyString())).thenReturn(categoriaCompleta(id));

        mockMvc.perform(get("/categorias/{id_categorias}/{categorias_code}", id, "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.intValue()));
    }
}

package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Consulta de categorias a serviço da importação de extratos: transforma o nome
 * sugerido pela IA ("alimentacao", "salario"...) na categoria real do banco e
 * resolve a categoria genérica quando nada casa.
 *
 * <p>Cada consulta busca só os tipos de que precisa — quem quer apenas o fallback
 * não paga a leitura das categorias de receita e gasto — e o resultado por tipo é
 * memorizado, então classificar 50 lançamentos não vira 50 idas ao banco.
 */
public final class CatalogoCategoriasImportacao {

    private final CategoriaRepositoryPort repository;
    private final Map<TipoCategoria, List<Categoria>> porTipo = new EnumMap<>(TipoCategoria.class);

    private CatalogoCategoriasImportacao(CategoriaRepositoryPort repository) {
        this.repository = repository;
    }

    private List<Categoria> listar(TipoCategoria tipo) {
        return porTipo.computeIfAbsent(tipo, repository::listarPorTipo);
    }

    public static CatalogoCategoriasImportacao carregar(CategoriaRepositoryPort repository) {
        return new CatalogoCategoriasImportacao(repository);
    }

    /**
     * Categoria genérica (tipo AMBOS) em que os lançamentos importados nascem
     * quando a IA não sugeriu nada aproveitável. Nela a direção do lançamento é
     * decidida pelo sinal do valor.
     */
    public Categoria fallback() {
        List<Categoria> ambos = listar(TipoCategoria.AMBOS);
        return ambos.stream()
                .filter(Categoria::isPadrao)
                .findFirst()
                .or(() -> ambos.stream().findFirst())
                .orElseThrow(() -> new CategoriaInvalidaException(
                        "Nenhuma categoria do tipo AMBOS cadastrada para classificar lançamentos importados"));
    }

    /**
     * Casa o nome sugerido pela IA com uma categoria compatível com a direção do
     * lançamento — primeiro entre as do tipo exato, depois entre as AMBOS.
     * Comparação sem acento e sem caixa.
     */
    public Optional<Categoria> porNomeSugerido(String nomeSugerido, TipoTransacao direcao) {
        String alvo = normalizar(nomeSugerido);
        if (alvo.isEmpty() || direcao == null) return Optional.empty();

        List<Categoria> candidatas = new ArrayList<>(listar(
                direcao == TipoTransacao.RECEITA ? TipoCategoria.RECEITA : TipoCategoria.GASTO));
        candidatas.addAll(listar(TipoCategoria.AMBOS));

        return candidatas.stream()
                .filter(c -> normalizar(c.getNome()).equals(alvo))
                .findFirst();
    }

    private static String normalizar(String valor) {
        if (valor == null) return "";
        return Normalizer.normalize(valor.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}

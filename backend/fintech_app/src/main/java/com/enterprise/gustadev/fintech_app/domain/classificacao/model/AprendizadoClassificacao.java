package com.enterprise.gustadev.fintech_app.domain.classificacao.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.text.Normalizer;
import java.time.OffsetDateTime;

/**
 * Memória do que o usuário decidiu sobre lançamentos com a mesma descrição — é o
 * que calibra a confiança que a IA atribui na leitura de um extrato.
 *
 * <p>Cada revisão confirmada soma um acerto para a direção (e categoria) escolhida;
 * cada revisão estornada soma um erro. O {@link #ajuste()} resultante é somado à
 * confiança crua da IA no próximo extrato com a mesma {@link #normalizarChave chave}.
 */
@Getter
@Setter
public class AprendizadoClassificacao {

    /** Quanto cada confirmação soma ao ajuste, até {@link #AJUSTE_MAXIMO}. */
    public static final int PESO_ACERTO = 4;
    /** Quanto cada revisão estornada tira do ajuste, até {@link #AJUSTE_MINIMO}. */
    public static final int PESO_ERRO = 15;
    public static final int AJUSTE_MAXIMO = 20;
    public static final int AJUSTE_MINIMO = -60;
    /** Penalidade quando a IA sugere uma direção diferente da que o usuário costuma escolher. */
    public static final int PENALIDADE_DIVERGENCIA = 30;
    private static final int TAMANHO_MAXIMO_CHAVE = 120;

    private Long id;
    private String code;
    private Long usuarioId;
    private String chave;
    private TipoTransacao tipo;
    private Long categoriaId;
    private String categoriaCode;
    private int acertos;
    private int erros;
    private OffsetDateTime atualizadoEm;

    public AprendizadoClassificacao(Long id, String code, Long usuarioId, String chave, TipoTransacao tipo,
                                    Long categoriaId, String categoriaCode, int acertos, int erros,
                                    OffsetDateTime atualizadoEm) {
        this.id = id;
        this.code = code;
        this.usuarioId = usuarioId;
        this.chave = chave;
        this.tipo = tipo;
        this.categoriaId = categoriaId;
        this.categoriaCode = categoriaCode;
        this.acertos = acertos;
        this.erros = erros;
        this.atualizadoEm = atualizadoEm;
    }

    public AprendizadoClassificacao(Long usuarioId, String chave, TipoTransacao tipo) {
        this(null, CodeGenerator.gerar(), usuarioId, chave, tipo, null, null, 0, 0, OffsetDateTime.now());
    }

    /**
     * Registra uma revisão confirmada. Se o usuário mudou de ideia sobre a direção,
     * o histórico anterior deixa de valer e o aprendizado recomeça pela nova escolha.
     */
    public void registrarAcerto(TipoTransacao tipoEscolhido, Long categoriaId, String categoriaCode) {
        if (tipoEscolhido != tipo) {
            this.tipo = tipoEscolhido;
            this.acertos = 0;
            this.erros = 0;
        }
        this.acertos++;
        this.categoriaId = categoriaId;
        this.categoriaCode = categoriaCode;
        this.atualizadoEm = OffsetDateTime.now();
    }

    /** Registra uma revisão estornada: a classificação confirmada antes estava errada. */
    public void registrarErro() {
        this.erros++;
        this.atualizadoEm = OffsetDateTime.now();
    }

    /** Pontos somados à confiança da IA quando ela sugere a mesma direção deste aprendizado. */
    public int ajuste() {
        int bonus = Math.min(AJUSTE_MAXIMO, acertos * PESO_ACERTO);
        int penalidade = erros * PESO_ERRO;
        return Math.max(AJUSTE_MINIMO, bonus - penalidade);
    }

    /**
     * Reduz a descrição a uma chave estável: minúsculas, sem acentos, sem dígitos nem
     * pontuação. Assim {@code Pix enviado: "Cp :18236120-Fulano"} e o mesmo Pix no mês
     * seguinte caem na mesma chave. Devolve {@code null} quando não sobra texto útil.
     */
    public static String normalizarChave(String texto) {
        if (texto == null) return null;
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String chave = semAcento.toLowerCase()
                .replaceAll("[^a-z ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (chave.isEmpty()) return null;
        return chave.length() > TAMANHO_MAXIMO_CHAVE ? chave.substring(0, TAMANHO_MAXIMO_CHAVE) : chave;
    }
}

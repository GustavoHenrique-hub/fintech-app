package com.enterprise.gustadev.fintech_app.domain.gasto.model;

import com.enterprise.gustadev.fintech_app.domain.gasto.exception.GastoInvalidoException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Gasto {

    private Long id;
    private Long usuarioId;
    private BigDecimal valor;
    private String descricao;
    private Categoria categoria;
    private LocalDate data;

    public Gasto(Long id, Long usuarioId, BigDecimal valor, String descricao, Categoria categoria, LocalDate data) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.descricao = descricao;
        this.categoria = categoria;
        this.data = data;
    }

    public Gasto(Long usuarioId, BigDecimal valor, String descricao, Categoria categoria, LocalDate data) {
        this(null, usuarioId, valor, descricao, categoria, data);
    }

    public void validar() {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new GastoInvalidoException("Valor deve ser maior que zero");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new GastoInvalidoException("Descricao e obrigatoria");
        }
        if (categoria == null) {
            throw new GastoInvalidoException("Categoria e obrigatoria");
        }
        if (data == null) {
            throw new GastoInvalidoException("Data e obrigatoria");
        }
        if (usuarioId == null || usuarioId <= 0) {
            throw new GastoInvalidoException("UsuarioId deve ser informado");
        }
    }
}

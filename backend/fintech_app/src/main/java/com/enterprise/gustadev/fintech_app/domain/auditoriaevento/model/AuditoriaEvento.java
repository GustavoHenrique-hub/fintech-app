package com.enterprise.gustadev.fintech_app.domain.auditoriaevento.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.AcaoAuditoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemAuditoria;
import com.enterprise.gustadev.fintech_app.domain.shared.util.CodeGenerator;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class AuditoriaEvento {

    private Long id;
    private String code;
    private Long correlationId;
    private Long usuarioId;
    private String usuarioCode;
    private String entidade;
    private Long entidadeId;
    private AcaoAuditoria acao;
    private String dadosAnteriores;
    private String dadosNovos;
    private String ipOrigem;
    private String userAgent;
    private OrigemAuditoria origem;
    private OffsetDateTime criadoEm;

    /**
     * Construtor de reconstrução (a partir da persistência).
     */
    public AuditoriaEvento(Long id, String code, Long correlationId, Long usuarioId,
                            String usuarioCode, String entidade,
                            Long entidadeId, AcaoAuditoria acao, String dadosAnteriores,
                            String dadosNovos, String ipOrigem, String userAgent,
                            OrigemAuditoria origem, OffsetDateTime criadoEm) {
        this.id = id;
        this.code = code;
        this.correlationId = correlationId;
        this.usuarioId = usuarioId;
        this.usuarioCode = usuarioCode;
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.acao = acao;
        this.dadosAnteriores = dadosAnteriores;
        this.dadosNovos = dadosNovos;
        this.ipOrigem = ipOrigem;
        this.userAgent = userAgent;
        this.origem = origem;
        this.criadoEm = criadoEm;
    }

    /**
     * Construtor de criação (gera novo {@code code}).
     */
    public AuditoriaEvento(Long correlationId, Long usuarioId, String usuarioCode, String entidade,
                            Long entidadeId, AcaoAuditoria acao, String dadosNovos,
                            String ipOrigem, OrigemAuditoria origem) {
        this(null, CodeGenerator.gerar(), correlationId, usuarioId, usuarioCode, entidade,
             entidadeId, acao, null, dadosNovos, ipOrigem, null, origem, null);
    }
}

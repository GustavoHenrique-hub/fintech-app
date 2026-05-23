package com.enterprise.gustadev.fintech_app.domain.auditoriaevento.model;

import com.enterprise.gustadev.fintech_app.domain.shared.enums.AcaoAuditoria;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemAuditoria;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class AuditoriaEvento {

    private Long id;
    private Long correlationId;
    private Long usuarioId;
    private String entidade;
    private Long entidadeId;
    private AcaoAuditoria acao;
    private String dadosAnteriores;
    private String dadosNovos;
    private String ipOrigem;
    private String userAgent;
    private OrigemAuditoria origem;
    private OffsetDateTime criadoEm;

    public AuditoriaEvento(Long id, Long correlationId, Long usuarioId, String entidade,
                            Long entidadeId, AcaoAuditoria acao, String dadosAnteriores,
                            String dadosNovos, String ipOrigem, String userAgent,
                            OrigemAuditoria origem, OffsetDateTime criadoEm) {
        this.id = id;
        this.correlationId = correlationId;
        this.usuarioId = usuarioId;
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

    public AuditoriaEvento(Long correlationId, Long usuarioId, String entidade,
                            Long entidadeId, AcaoAuditoria acao, String dadosNovos,
                            String ipOrigem, OrigemAuditoria origem) {
        this(null, correlationId, usuarioId, entidade, entidadeId, acao,
             null, dadosNovos, ipOrigem, null, origem, null);
    }
}

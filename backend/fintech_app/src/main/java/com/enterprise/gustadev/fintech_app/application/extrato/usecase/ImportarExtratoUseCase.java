package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.application.extrato.parser.DetectorFormatoExtrato;
import com.enterprise.gustadev.fintech_app.application.extrato.parser.ExtratoParser;
import com.enterprise.gustadev.fintech_app.application.extrato.parser.LancamentoExtraido;
import com.enterprise.gustadev.fintech_app.domain.categoria.exception.CategoriaInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.categoria.model.Categoria;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ArmazenamentoArquivoPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.FormatoExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusExtrato;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.StatusRevisaoTransacao;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoCategoria;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * Recebe o extrato bruto (PDF/CSV/TXT/XLS/XLSX), extrai os lançamentos por formato e
 * cria as transações com {@code statusRevisao=PENDENTE_REVISAO} numa categoria
 * genérica — a classificação por IA (categoria final, confiança) é um passo
 * posterior, ainda não automatizado nesta versão.
 */
public class ImportarExtratoUseCase {

    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final CategoriaRepositoryPort categoriaRepository;
    private final TransacaoRepositoryPort transacaoRepository;
    private final ArmazenamentoArquivoPort armazenamento;
    private final List<ExtratoParser> parsers;

    public ImportarExtratoUseCase(ExtratoRepositoryPort extratoRepository,
                                   ContaFinanceiraRepositoryPort contaRepository,
                                   CategoriaRepositoryPort categoriaRepository,
                                   TransacaoRepositoryPort transacaoRepository,
                                   ArmazenamentoArquivoPort armazenamento,
                                   List<ExtratoParser> parsers) {
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
        this.transacaoRepository = transacaoRepository;
        this.armazenamento = armazenamento;
        this.parsers = parsers;
    }

    @Transactional
    public Extrato executar(Long usuarioId, Long contaId, String nomeArquivo, byte[] conteudo) {
        if (conteudo == null || conteudo.length == 0) {
            throw new ExtratoInvalidoException("Arquivo vazio");
        }

        ContaFinanceira conta = contaRepository.buscarPorId(contaId)
                .orElseThrow(() -> new ContaFinanceiraInvalidaException(
                        "Conta financeira não encontrada: " + contaId));
        if (!conta.getUsuarioId().equals(usuarioId)) {
            throw new ExtratoInvalidoException("Conta financeira não pertence ao usuário informado");
        }

        FormatoExtrato formato = DetectorFormatoExtrato.detectar(nomeArquivo);
        String hash = calcularHash(conteudo);
        extratoRepository.buscarPorHash(hash).ifPresent(e -> {
            throw new ExtratoInvalidoException("Extrato duplicado: hash já processado");
        });

        String arquivoUuid = UUID.randomUUID().toString();
        armazenamento.salvar(arquivoUuid, nomeArquivo, conteudo);

        Extrato extrato = new Extrato(usuarioId, conta.getUsuarioCode(), conta.getId(), conta.getCode(),
                nomeArquivo, arquivoUuid, hash);
        extrato.validar();
        extrato = extratoRepository.salvar(extrato);

        ExtratoParser parser = parsers.stream()
                .filter(p -> p.suporta(formato))
                .findFirst()
                .orElseThrow(() -> new ExtratoInvalidoException(
                        "Nenhum parser disponível para o formato " + formato));

        List<LancamentoExtraido> lancamentos;
        try {
            lancamentos = parser.parsear(conteudo);
        } catch (ExtratoInvalidoException e) {
            extrato.setStatus(StatusExtrato.erro_extracao);
            extratoRepository.salvar(extrato);
            throw e;
        }

        Categoria categoriaFallback = resolverCategoriaFallback();
        int criadas = 0;
        for (LancamentoExtraido lancamento : lancamentos) {
            if (lancamento.valor().signum() == 0) continue;

            Transacao transacao = new Transacao(conta, lancamento.valor(), lancamento.data(),
                    categoriaFallback.getId(), categoriaFallback.getCode(), OrigemTransacao.importado);
            transacao.setDescricao(lancamento.descricao());
            transacao.setCategoriaTipo(categoriaFallback.getTipo());
            transacao.setStatusRevisao(StatusRevisaoTransacao.PENDENTE_REVISAO);
            transacao.setExtratoId(extrato.getId());
            transacao.setExtratoCode(extrato.getCode());
            transacao.validar();

            transacaoRepository.salvar(transacao);
            conta.aplicarTransacao(transacao.tipoEfetivo(), transacao.getValor().abs());
            criadas++;
        }
        contaRepository.salvar(conta);

        extrato.setTotalLancamentos(criadas);
        extrato.setLancamentosPendentes(criadas);
        extrato.setStatus(criadas > 0 ? StatusExtrato.pendente_revisao : StatusExtrato.erro_extracao);
        return extratoRepository.salvar(extrato);
    }

    private Categoria resolverCategoriaFallback() {
        List<Categoria> categoriasAmbos = categoriaRepository.listarPorTipo(TipoCategoria.AMBOS);
        return categoriasAmbos.stream()
                .filter(Categoria::isPadrao)
                .findFirst()
                .or(() -> categoriasAmbos.stream().findFirst())
                .orElseThrow(() -> new CategoriaInvalidaException(
                        "Nenhuma categoria do tipo AMBOS cadastrada para classificar lançamentos importados"));
    }

    private String calcularHash(byte[] conteudo) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(conteudo);
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível", e);
        }
    }
}

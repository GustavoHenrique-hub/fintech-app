package com.enterprise.gustadev.fintech_app.application.extrato.usecase;

import com.enterprise.gustadev.fintech_app.application.extrato.parser.DetectorFormatoExtrato;
import com.enterprise.gustadev.fintech_app.application.extrato.parser.ExtratoParser;
import com.enterprise.gustadev.fintech_app.domain.categoria.port.CategoriaRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.exception.ContaFinanceiraInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.port.ContaFinanceiraRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ArmazenamentoArquivoPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ExtratoRepositoryPort;
import com.enterprise.gustadev.fintech_app.domain.extrato.port.ProcessamentoExtratoPort;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * Recebe o extrato bruto (PDF/CSV/TXT/XLS/XLSX) e o encaminha para quem sabe
 * extrair os lançamentos:
 *
 * <ul>
 *   <li><b>PDF</b> → automação N8N + IA ({@link ProcessamentoExtratoPort}). O extrato
 *       fica {@code na_fila} e as transações nascem depois, no callback
 *       ({@code POST /extratos/{id}/callback}).</li>
 *   <li><b>CSV/TXT/XLS/XLSX</b> → parser local, síncrono (a automação só entende
 *       PDF e imagem — ver o nó "Preparar arquivo" do workflow 01-extratos-core-ia).</li>
 * </ul>
 *
 * Se o envio ao N8N falhar (automação desligada ou fora do ar), o PDF cai no
 * parser local — a importação nunca fica sem resposta.
 *
 * <p>Os lançamentos criados aqui nascem em uma categoria genérica com
 * {@code statusRevisao=PENDENTE_REVISAO}: o tipo final (gasto/receita/economias)
 * é escolhido pelo usuário na revisão do extrato.
 */
public class ImportarExtratoUseCase {

    private final ExtratoRepositoryPort extratoRepository;
    private final ContaFinanceiraRepositoryPort contaRepository;
    private final ArmazenamentoArquivoPort armazenamento;
    private final EncaminhamentoExtrato encaminhamento;

    public ImportarExtratoUseCase(ExtratoRepositoryPort extratoRepository,
                                   ContaFinanceiraRepositoryPort contaRepository,
                                   CategoriaRepositoryPort categoriaRepository,
                                   TransacaoRepositoryPort transacaoRepository,
                                   ArmazenamentoArquivoPort armazenamento,
                                   ProcessamentoExtratoPort processamento,
                                   List<ExtratoParser> parsers) {
        this.extratoRepository = extratoRepository;
        this.contaRepository = contaRepository;
        this.armazenamento = armazenamento;
        this.encaminhamento = new EncaminhamentoExtrato(extratoRepository, contaRepository,
                categoriaRepository, transacaoRepository, processamento, parsers);
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

        // Falha cedo para formato não suportado, antes de gravar qualquer coisa.
        DetectorFormatoExtrato.detectar(nomeArquivo);
        String hash = calcularHash(conteudo);
        extratoRepository.buscarPorHash(hash).ifPresent(e -> {
            throw new ExtratoInvalidoException("Extrato duplicado: este arquivo já foi importado. Abra-o na lista de extratos e use Reenviar");
        });

        String arquivoUuid = UUID.randomUUID().toString();
        armazenamento.salvar(arquivoUuid, nomeArquivo, conteudo);

        Extrato extrato = new Extrato(usuarioId, conta.getUsuarioCode(), conta.getId(), conta.getCode(),
                nomeArquivo, arquivoUuid, hash);
        extrato.validar();
        extrato = extratoRepository.salvar(extrato);

        return encaminhamento.encaminhar(extrato, conta, conteudo);
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

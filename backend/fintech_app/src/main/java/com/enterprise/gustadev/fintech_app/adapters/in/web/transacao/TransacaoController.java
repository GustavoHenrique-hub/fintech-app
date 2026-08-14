package com.enterprise.gustadev.fintech_app.adapters.in.web.transacao;

import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.EstornarTransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.ResumoPeriodoResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.RevisarTransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.transacao.dto.TransacaoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.BuscarResumoPeriodoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.BuscarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ConfirmarRevisaoTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.CriarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.EstornarTransacaoUseCase;
import com.enterprise.gustadev.fintech_app.application.transacao.usecase.ListarTransacoesUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.OrigemTransacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Transações", description = "Registro e consulta de transações financeiras (débito, crédito, transferência)")
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CriarTransacaoUseCase criarUseCase;
    private final ListarTransacoesUseCase listarUseCase;
    private final BuscarTransacaoUseCase buscarUseCase;
    private final EstornarTransacaoUseCase estornarUseCase;
    private final BuscarResumoPeriodoUseCase resumoPeriodoUseCase;
    private final ConfirmarRevisaoTransacaoUseCase confirmarRevisaoUseCase;

    public TransacaoController(CriarTransacaoUseCase criarUseCase,
                               ListarTransacoesUseCase listarUseCase,
                               BuscarTransacaoUseCase buscarUseCase,
                               EstornarTransacaoUseCase estornarUseCase,
                               BuscarResumoPeriodoUseCase resumoPeriodoUseCase,
                               ConfirmarRevisaoTransacaoUseCase confirmarRevisaoUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.estornarUseCase = estornarUseCase;
        this.resumoPeriodoUseCase = resumoPeriodoUseCase;
        this.confirmarRevisaoUseCase = confirmarRevisaoUseCase;
    }

    @Operation(summary = "Criar transação", description = "Registra uma nova transação financeira. O campo origem deve conter um valor válido do enum OrigemTransacao. A direção (receita/gasto) é derivada da categoria informada; para categorias do tipo AMBOS, o sinal de valor decide (negativo = gasto).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        ContaFinanceira conta = new ContaFinanceira(dto.contaId(), dto.contaCode());
        Transacao transacao = new Transacao(
                conta,
                dto.valor(), dto.dataTransacao(),
                dto.categoriaId(),
                dto.categoriaCode(),
                OrigemTransacao.valueOf(dto.origem())
        );
        transacao.setDescricao(dto.descricao());
        transacao.setEstabelecimento(dto.estabelecimento());
        transacao.setObservacao(dto.observacao());
        TransacaoResponseDTO response = TransacaoResponseDTO.fromDomain(criarUseCase.executar(transacao));
        return ResponseEntity.created(URI.create("/transacoes/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar transações do usuário", description = "Retorna todas as transações de um usuário, independente da conta.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarUseCase.executarPorUsuario(usuarioId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Listar transações por conta", description = "Retorna todas as transações vinculadas a uma conta financeira específica.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorConta(
            @Parameter(description = "ID da conta") @PathVariable Long contaId) {
        return ResponseEntity.ok(listarUseCase.executarPorConta(contaId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Listar transações de um extrato", description = "Retorna os lançamentos criados a partir de um extrato importado, para a tela de revisão manual.")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @GetMapping("/extrato/{extratoId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorExtrato(
            @Parameter(description = "ID do extrato") @PathVariable Long extratoId) {
        return ResponseEntity.ok(listarUseCase.executarPorExtrato(extratoId).stream()
                .map(TransacaoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar transação por ID e código",
            description = "Retorna a transação identificada pela chave composta (id_transacoes + transacoes_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{id_transacoes}/{transacoes_code}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID da transação (id_transacoes)") @PathVariable("id_transacoes") Long idTransacoes,
            @Parameter(description = "Código alfanumérico de 6 caracteres (transacoes_code)") @PathVariable("transacoes_code") String transacoesCode) {
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(buscarUseCase.executar(idTransacoes, transacoesCode)));
    }

    @Operation(summary = "Estornar transação",
            description = "Marca a transação identificada pela chave composta como indEstorno='S'. Operação idempotente: chamar novamente em uma transação já estornada retorna 200 sem nova alteração.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação estornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PatchMapping("/{id_transacoes}/{transacoes_code}/estornar")
    public ResponseEntity<TransacaoResponseDTO> estornar(
            @Parameter(description = "ID da transação (id_transacoes)") @PathVariable("id_transacoes") Long idTransacoes,
            @Parameter(description = "Código alfanumérico de 6 caracteres (transacoes_code)") @PathVariable("transacoes_code") String transacoesCode,
            @Valid @RequestBody EstornarTransacaoRequestDTO dto) {
        Transacao estornada = estornarUseCase.executar(
                idTransacoes, transacoesCode,
                dto.contaId(), dto.contaCode());
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(estornada));
    }

    @Operation(summary = "Confirmar revisão de um lançamento importado",
            description = "Fecha a revisão de um lançamento pendente vindo de extrato. No corpo (opcional) o usuário " +
                    "informa o destino escolhido na tela de revisão: GASTO ou RECEITA confirmam a transação " +
                    "(ajustando categoria e saldo quando a direção muda); ECONOMIA converte o valor em aporte no " +
                    "sub-saldo de economias da conta e tira a transação das listagens. Sem corpo, mantém a " +
                    "classificação que veio do extrato. Só é permitido a partir do status PENDENTE_REVISAO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Revisão confirmada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transação não está pendente de revisão, destino ou categoria inválidos"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PatchMapping("/{id_transacoes}/{transacoes_code}/revisar")
    public ResponseEntity<TransacaoResponseDTO> revisar(
            @Parameter(description = "ID da transação (id_transacoes)") @PathVariable("id_transacoes") Long idTransacoes,
            @Parameter(description = "Código alfanumérico de 6 caracteres (transacoes_code)") @PathVariable("transacoes_code") String transacoesCode,
            @RequestBody(required = false) RevisarTransacaoRequestDTO dto) {
        Transacao revisada = dto == null
                ? confirmarRevisaoUseCase.executar(idTransacoes, transacoesCode)
                : confirmarRevisaoUseCase.executar(idTransacoes, transacoesCode,
                        dto.destinoDomain(), dto.categoriaId(), dto.categoriaCode());
        return ResponseEntity.ok(TransacaoResponseDTO.fromDomain(revisada));
    }

    @Operation(summary = "Resumo por período e conta",
            description = "Soma o total de receitas e gastos de uma conta em um intervalo de datas arbitrário " +
                    "(semana, mês, ano ou qualquer outro período). Transações estornadas não entram na soma.")
    @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso")
    @GetMapping("/resumo-periodo")
    public ResponseEntity<ResumoPeriodoResponseDTO> resumoPeriodo(
            @Parameter(description = "ID do usuário") @RequestParam Long usuarioId,
            @Parameter(description = "Código do usuário") @RequestParam String usuarioCode,
            @Parameter(description = "ID da conta") @RequestParam Long contaId,
            @Parameter(description = "Código da conta") @RequestParam String contaCode,
            @Parameter(description = "Início do período (inclusive)") @RequestParam LocalDate inicio,
            @Parameter(description = "Fim do período (inclusive)") @RequestParam LocalDate fim) {
        return ResponseEntity.ok(ResumoPeriodoResponseDTO.fromDomain(
                resumoPeriodoUseCase.executar(usuarioId, usuarioCode, contaId, contaCode, inicio, fim)));
    }
}

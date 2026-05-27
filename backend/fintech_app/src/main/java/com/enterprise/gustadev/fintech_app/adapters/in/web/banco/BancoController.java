package com.enterprise.gustadev.fintech_app.adapters.in.web.banco;

import com.enterprise.gustadev.fintech_app.adapters.in.web.banco.dto.BancoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.banco.dto.BancoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.banco.usecase.BuscarBancoUseCase;
import com.enterprise.gustadev.fintech_app.application.banco.usecase.CriarBancoUseCase;
import com.enterprise.gustadev.fintech_app.application.banco.usecase.DeletarBancoUseCase;
import com.enterprise.gustadev.fintech_app.application.banco.usecase.ListarBancosUseCase;
import com.enterprise.gustadev.fintech_app.domain.banco.model.Banco;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Bancos",
        description = "Catálogo de bancos disponíveis para vínculo com contas financeiras. " +
                "Um mesmo banco pode estar vinculado a contas de vários usuários através da tabela " +
                "`contas_financeiras` (relação N:N entre usuário e banco). " +
                "Use o par retornado `banco_id` + `banco_code` como chave para criar/atualizar contas.")
@RestController
@RequestMapping("/bancos")
public class BancoController {

    private final CriarBancoUseCase criarUseCase;
    private final ListarBancosUseCase listarUseCase;
    private final BuscarBancoUseCase buscarUseCase;
    private final DeletarBancoUseCase deletarUseCase;

    public BancoController(CriarBancoUseCase criarUseCase,
                           ListarBancosUseCase listarUseCase,
                           BuscarBancoUseCase buscarUseCase,
                           DeletarBancoUseCase deletarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
    }

    @Operation(summary = "Criar banco",
            description = "Cadastra um novo banco no catálogo. O `banco_code` (6 caracteres alfanuméricos) " +
                    "é gerado automaticamente e, junto com `banco_id`, forma a chave composta usada para " +
                    "vincular contas em `POST /contas` (campos `bancoId` e `bancoCode`).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Banco criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (ex.: nome em branco)")
    })
    @PostMapping
    public ResponseEntity<BancoResponseDTO> criar(@Valid @RequestBody BancoRequestDTO dto) {
        Banco banco = new Banco(dto.nome(), dto.descricao(), dto.corHex(), dto.icone());
        BancoResponseDTO response = BancoResponseDTO.fromDomain(criarUseCase.executar(banco));
        return ResponseEntity.created(URI.create("/bancos/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar bancos",
            description = "Retorna todos os bancos cadastrados. Útil para popular o seletor de banco na criação de uma conta.")
    @ApiResponse(responseCode = "200", description = "Lista de bancos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<BancoResponseDTO>> listar() {
        return ResponseEntity.ok(listarUseCase.executar().stream()
                .map(BancoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar banco por ID e código",
            description = "Retorna os dados de um banco identificado pela chave composta (`banco_id` + `banco_code`).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Banco encontrado"),
            @ApiResponse(responseCode = "404", description = "Banco não encontrado")
    })
    @GetMapping("/{banco_id}/{banco_code}")
    public ResponseEntity<BancoResponseDTO> buscarPorId(
            @Parameter(description = "ID do banco (banco_id)") @PathVariable("banco_id") Long bancoId,
            @Parameter(description = "Código alfanumérico de 6 caracteres (banco_code)") @PathVariable("banco_code") String bancoCode) {
        return ResponseEntity.ok(BancoResponseDTO.fromDomain(buscarUseCase.executar(bancoId, bancoCode)));
    }

    @Operation(summary = "Deletar banco",
            description = "Remove permanentemente o banco identificado pela chave composta (`banco_id` + `banco_code`). " +
                    "Atenção: a operação falhará se houver contas financeiras vinculadas a este banco (FK).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Banco removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Banco não encontrado")
    })
    @DeleteMapping("/{banco_id}/{banco_code}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do banco (banco_id)") @PathVariable("banco_id") Long bancoId,
            @Parameter(description = "Código alfanumérico de 6 caracteres (banco_code)") @PathVariable("banco_code") String bancoCode) {
        deletarUseCase.executar(bancoId, bancoCode);
        return ResponseEntity.noContent().build();
    }
}

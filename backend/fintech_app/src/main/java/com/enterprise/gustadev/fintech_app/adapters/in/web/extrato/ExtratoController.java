package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato;

import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.BuscarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ListarExtratosUseCase;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Extratos", description = "Upload e consulta de extratos bancários importados pelo usuário")
@RestController
@RequestMapping("/extratos")
public class ExtratoController {

    private final CriarExtratoUseCase criarUseCase;
    private final ListarExtratosUseCase listarUseCase;
    private final BuscarExtratoUseCase buscarUseCase;

    public ExtratoController(CriarExtratoUseCase criarUseCase,
                              ListarExtratosUseCase listarUseCase,
                              BuscarExtratoUseCase buscarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
    }

    @Operation(summary = "Criar extrato", description = "Registra os metadados de um extrato bancário importado (nome, UUID e hash do arquivo).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Extrato criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<ExtratoResponseDTO> criar(@Valid @RequestBody ExtratoRequestDTO dto) {
        Extrato extrato = new Extrato(
                dto.usuarioId(), dto.contaId(),
                dto.arquivoNome(), dto.arquivoUuid(), dto.hashArquivo()
        );
        ExtratoResponseDTO response = ExtratoResponseDTO.fromDomain(criarUseCase.executar(extrato));
        return ResponseEntity.created(URI.create("/extratos/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar extratos do usuário", description = "Retorna todos os extratos importados por um usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de extratos retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ExtratoResponseDTO>> listarPorUsuario(
            @Parameter(description = "UUID do usuário") @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(ExtratoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Buscar extrato por ID e código",
            description = "Retorna o extrato identificado pela chave composta (id_extratos + extratos_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extrato encontrado"),
            @ApiResponse(responseCode = "404", description = "Extrato não encontrado")
    })
    @GetMapping("/{id_extratos}/{extratos_code}")
    public ResponseEntity<ExtratoResponseDTO> buscarPorId(
            @Parameter(description = "UUID do extrato (id_extratos)") @PathVariable("id_extratos") UUID idExtratos,
            @Parameter(description = "Código alfanumérico de 6 caracteres (extratos_code)") @PathVariable("extratos_code") String extratosCode) {
        return ResponseEntity.ok(ExtratoResponseDTO.fromDomain(buscarUseCase.executar(idExtratos, extratosCode)));
    }
}

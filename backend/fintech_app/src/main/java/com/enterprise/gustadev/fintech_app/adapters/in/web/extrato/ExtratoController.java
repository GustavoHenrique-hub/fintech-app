package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato;

import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.BuscarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ImportarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ListarExtratosUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.RemoverExtratoUseCase;
import com.enterprise.gustadev.fintech_app.domain.extrato.exception.ExtratoInvalidoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.List;

@Tag(name = "Extratos", description = "Upload e consulta de extratos bancários importados pelo usuário")
@RestController
@RequestMapping("/extratos")
public class ExtratoController {

    private final CriarExtratoUseCase criarUseCase;
    private final ImportarExtratoUseCase importarUseCase;
    private final ListarExtratosUseCase listarUseCase;
    private final BuscarExtratoUseCase buscarUseCase;
    private final RemoverExtratoUseCase removerUseCase;

    public ExtratoController(CriarExtratoUseCase criarUseCase,
                              ImportarExtratoUseCase importarUseCase,
                              ListarExtratosUseCase listarUseCase,
                              BuscarExtratoUseCase buscarUseCase,
                              RemoverExtratoUseCase removerUseCase) {
        this.criarUseCase = criarUseCase;
        this.importarUseCase = importarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.removerUseCase = removerUseCase;
    }

    @Operation(summary = "Upload de extrato", description = "Recebe o arquivo do extrato bancário (PDF, CSV, TXT, XLS ou XLSX), "
            + "extrai os lançamentos e cria as transações correspondentes com status PENDENTE_REVISAO.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Extrato importado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido, formato não suportado ou duplicado")
    })
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ExtratoResponseDTO> upload(
            @RequestParam Long usuarioId,
            @RequestParam Long contaId,
            @RequestParam("arquivo") MultipartFile arquivo) {
        byte[] conteudo;
        try {
            conteudo = arquivo.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível ler o arquivo enviado", e);
        }
        if (arquivo.getOriginalFilename() == null || arquivo.getOriginalFilename().isBlank()) {
            throw new ExtratoInvalidoException("Nome do arquivo é obrigatório");
        }

        ExtratoResponseDTO response = ExtratoResponseDTO.fromDomain(
                importarUseCase.executar(usuarioId, contaId, arquivo.getOriginalFilename(), conteudo));
        return ResponseEntity.created(URI.create("/extratos/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Criar extrato", description = "Registra os metadados de um extrato bancário importado (nome_completo, ID e hash do arquivo).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Extrato criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<ExtratoResponseDTO> criar(@Valid @RequestBody ExtratoRequestDTO dto) {
        ExtratoResponseDTO response = ExtratoResponseDTO.fromDomain(
                criarUseCase.executar(dto.usuarioId(), dto.contaId(),
                        dto.arquivoNome(), dto.arquivoUuid(), dto.hashArquivo()));
        return ResponseEntity.created(URI.create("/extratos/" + response.id() + "/" + response.code())).body(response);
    }

    @Operation(summary = "Listar extratos do usuário", description = "Retorna todos os extratos importados por um usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de extratos retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ExtratoResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(ExtratoResponseDTO::fromDomain).toList());
    }

    @Operation(summary = "Remover extrato (soft delete)",
            description = "Marca o extrato como removido (ind_delete='S') sem apagar o registro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extrato removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Extrato não encontrado")
    })
    @PatchMapping("/{id_extratos}/{extratos_code}/remover")
    public ResponseEntity<ExtratoResponseDTO> remover(
            @Parameter(description = "ID do extrato (id_extratos)") @PathVariable("id_extratos") Long idExtratos,
            @Parameter(description = "Código alfanumérico de 6 caracteres (extratos_code)") @PathVariable("extratos_code") String extratosCode) {
        return ResponseEntity.ok(ExtratoResponseDTO.fromDomain(removerUseCase.executar(idExtratos, extratosCode)));
    }

    @Operation(summary = "Buscar extrato por ID e código",
            description = "Retorna o extrato identificado pela chave composta (id_extratos + extratos_code).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extrato encontrado"),
            @ApiResponse(responseCode = "404", description = "Extrato não encontrado")
    })
    @GetMapping("/{id_extratos}/{extratos_code}")
    public ResponseEntity<ExtratoResponseDTO> buscarPorId(
            @Parameter(description = "ID do extrato (id_extratos)") @PathVariable("id_extratos") Long idExtratos,
            @Parameter(description = "Código alfanumérico de 6 caracteres (extratos_code)") @PathVariable("extratos_code") String extratosCode) {
        return ResponseEntity.ok(ExtratoResponseDTO.fromDomain(buscarUseCase.executar(idExtratos, extratosCode)));
    }
}

package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato;

import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.AtualizarStatusExtratoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.CallbackExtratoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.AtualizarStatusExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.BuscarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ImportarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ListarExtratosUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.RegistrarResultadoExtratoUseCase;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

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
    private final AtualizarStatusExtratoUseCase atualizarStatusUseCase;
    private final RegistrarResultadoExtratoUseCase registrarResultadoUseCase;
    private final AutenticacaoCallbackN8n autenticacaoCallback;
    private final ObjectMapper objectMapper;

    public ExtratoController(CriarExtratoUseCase criarUseCase,
                              ImportarExtratoUseCase importarUseCase,
                              ListarExtratosUseCase listarUseCase,
                              BuscarExtratoUseCase buscarUseCase,
                              RemoverExtratoUseCase removerUseCase,
                              AtualizarStatusExtratoUseCase atualizarStatusUseCase,
                              RegistrarResultadoExtratoUseCase registrarResultadoUseCase,
                              AutenticacaoCallbackN8n autenticacaoCallback,
                              ObjectMapper objectMapper) {
        this.criarUseCase = criarUseCase;
        this.importarUseCase = importarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.removerUseCase = removerUseCase;
        this.atualizarStatusUseCase = atualizarStatusUseCase;
        this.registrarResultadoUseCase = registrarResultadoUseCase;
        this.autenticacaoCallback = autenticacaoCallback;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Upload de extrato", description = "Recebe o arquivo do extrato bancário (PDF, CSV, TXT, XLS ou XLSX). "
            + "PDF é encaminhado para a automação N8N + IA e volta com status 'na_fila' — as transações aparecem depois, "
            + "pelo callback. Os demais formatos são lidos pelo parser local na hora e já devolvem os lançamentos "
            + "criados com status PENDENTE_REVISAO. Se a automação estiver indisponível, o PDF também cai no parser local.")
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

    @Operation(summary = "[N8N] Atualizar status do processamento",
            description = "Chamado pela automação (N8N) enquanto lê o arquivo, para a tela de extratos acompanhar "
                    + "o progresso (extraindo, classificando...). Autenticado por X-Internal-Api-Key — não usa sessão de usuário. "
                    + "Extrato que já saiu do processamento é devolvido sem alteração.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Status inválido ou extrato inexistente"),
            @ApiResponse(responseCode = "401", description = "Chave interna inválida")
    })
    @PatchMapping("/{id_extratos}/status")
    public ResponseEntity<ExtratoResponseDTO> atualizarStatus(
            @Parameter(description = "ID do extrato (id_extratos)") @PathVariable("id_extratos") Long idExtratos,
            @RequestHeader(value = "X-Internal-Api-Key", required = false) String chaveInterna,
            @Valid @RequestBody AtualizarStatusExtratoRequestDTO dto) {
        autenticacaoCallback.exigirChaveInterna(chaveInterna);
        return ResponseEntity.ok(ExtratoResponseDTO.fromDomain(
                atualizarStatusUseCase.executar(idExtratos, dto.status())));
    }

    @Operation(summary = "[N8N] Receber o resultado do processamento",
            description = "Callback da automação com os lançamentos extraídos e classificados pela IA. "
                    + "Cria as transações com status PENDENTE_REVISAO, atualiza o saldo da conta e fecha os contadores "
                    + "do extrato. Autenticado por X-Internal-Api-Key + assinatura HMAC-SHA256 do corpo bruto "
                    + "(X-N8N-Signature: sha256=<hex>). Idempotente: reenvio do mesmo callback não duplica lançamentos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado aplicado (ou ignorado, em caso de reenvio)"),
            @ApiResponse(responseCode = "400", description = "Payload inválido ou extrato inexistente"),
            @ApiResponse(responseCode = "401", description = "Chave interna ou assinatura inválida")
    })
    @PostMapping(value = "/{id_extratos}/callback", consumes = "application/json")
    public ResponseEntity<ExtratoResponseDTO> callbackProcessamento(
            @Parameter(description = "ID do extrato (id_extratos)") @PathVariable("id_extratos") Long idExtratos,
            @RequestHeader(value = "X-Internal-Api-Key", required = false) String chaveInterna,
            @RequestHeader(value = "X-N8N-Signature", required = false) String assinatura,
            @RequestBody String corpoBruto) {
        autenticacaoCallback.exigirChaveInterna(chaveInterna);
        // A assinatura cobre o corpo exatamente como trafegou — por isso ele é lido
        // como String e desserializado aqui, e não por um @RequestBody tipado.
        autenticacaoCallback.exigirAssinatura(corpoBruto, assinatura);

        CallbackExtratoRequestDTO dto;
        try {
            dto = objectMapper.readValue(corpoBruto, CallbackExtratoRequestDTO.class);
        } catch (JacksonException e) {
            throw new ExtratoInvalidoException("Payload do callback inválido: " + e.getMessage());
        }

        return ResponseEntity.ok(ExtratoResponseDTO.fromDomain(
                registrarResultadoUseCase.executar(idExtratos, dto.toDomain())));
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

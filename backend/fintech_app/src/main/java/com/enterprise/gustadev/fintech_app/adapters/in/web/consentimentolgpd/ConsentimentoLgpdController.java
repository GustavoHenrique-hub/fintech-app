package com.enterprise.gustadev.fintech_app.adapters.in.web.consentimentolgpd;

import com.enterprise.gustadev.fintech_app.adapters.in.web.consentimentolgpd.dto.ConsentimentoLgpdRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.consentimentolgpd.dto.ConsentimentoLgpdResponseDTO;
import com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase.ListarConsentimentosLgpdUseCase;
import com.enterprise.gustadev.fintech_app.application.consentimentolgpd.usecase.RegistrarConsentimentoLgpdUseCase;
import com.enterprise.gustadev.fintech_app.domain.consentimentolgpd.model.ConsentimentoLgpd;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConsentimentoLgpd;
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

@Tag(name = "Consentimentos LGPD", description = "Registro e consulta de consentimentos de privacidade conforme a LGPD")
@RestController
@RequestMapping("/consentimentos")
public class ConsentimentoLgpdController {

    private final RegistrarConsentimentoLgpdUseCase registrarUseCase;
    private final ListarConsentimentosLgpdUseCase listarUseCase;

    public ConsentimentoLgpdController(RegistrarConsentimentoLgpdUseCase registrarUseCase,
                                        ListarConsentimentosLgpdUseCase listarUseCase) {
        this.registrarUseCase = registrarUseCase;
        this.listarUseCase = listarUseCase;
    }

    @Operation(summary = "Registrar consentimento LGPD", description = "Registra o consentimento ou revogação de privacidade de um usuário. Tipo deve ser um dos valores de TipoConsentimentoLgpd.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consentimento registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    })
    @PostMapping
    public ResponseEntity<ConsentimentoLgpdResponseDTO> registrar(@Valid @RequestBody ConsentimentoLgpdRequestDTO dto) {
        ConsentimentoLgpd consentimento = new ConsentimentoLgpd(
                dto.usuarioId(),
                TipoConsentimentoLgpd.valueOf(dto.tipo()),
                dto.versaoPolitica(),
                dto.consentido(),
                dto.ipOrigem()
        );
        ConsentimentoLgpdResponseDTO response = ConsentimentoLgpdResponseDTO.fromDomain(registrarUseCase.executar(consentimento));
        return ResponseEntity.created(URI.create("/consentimentos/" + response.id())).body(response);
    }

    @Operation(summary = "Listar consentimentos do usuário", description = "Retorna todo o histórico de consentimentos LGPD de um usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de consentimentos retornada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ConsentimentoLgpdResponseDTO>> listarPorUsuario(
            @Parameter(description = "UUID do usuário") @PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(ConsentimentoLgpdResponseDTO::fromDomain).toList());
    }
}

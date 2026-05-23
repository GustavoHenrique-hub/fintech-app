package com.enterprise.gustadev.fintech_app.adapters.in.web.extrato;

import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.dto.ExtratoResponseDTO;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.BuscarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.CriarExtratoUseCase;
import com.enterprise.gustadev.fintech_app.application.extrato.usecase.ListarExtratosUseCase;
import com.enterprise.gustadev.fintech_app.domain.extrato.model.Extrato;
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

    @PostMapping
    public ResponseEntity<ExtratoResponseDTO> criar(@Valid @RequestBody ExtratoRequestDTO dto) {
        Extrato extrato = new Extrato(
                dto.usuarioId(), dto.contaId(),
                dto.arquivoNome(), dto.arquivoUuid(), dto.hashArquivo()
        );
        ExtratoResponseDTO response = ExtratoResponseDTO.fromDomain(criarUseCase.executar(extrato));
        return ResponseEntity.created(URI.create("/extratos/" + response.id())).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ExtratoResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(listarUseCase.executar(usuarioId).stream()
                .map(ExtratoResponseDTO::fromDomain).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtratoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ExtratoResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }
}

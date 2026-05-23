package com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira;

import com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto.ContaFinanceiraRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.contafinanceira.dto.ContaFinanceiraResponseDTO;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.BuscarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.CriarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.DeletarContaFinanceiraUseCase;
import com.enterprise.gustadev.fintech_app.application.contafinanceira.usecase.ListarContasFinanceirasUseCase;
import com.enterprise.gustadev.fintech_app.domain.contafinanceira.model.ContaFinanceira;
import com.enterprise.gustadev.fintech_app.domain.shared.enums.TipoConta;
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
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaFinanceiraController {

    private final CriarContaFinanceiraUseCase criarUseCase;
    private final ListarContasFinanceirasUseCase listarUseCase;
    private final BuscarContaFinanceiraUseCase buscarUseCase;
    private final DeletarContaFinanceiraUseCase deletarUseCase;

    public ContaFinanceiraController(CriarContaFinanceiraUseCase criarUseCase,
                                      ListarContasFinanceirasUseCase listarUseCase,
                                      BuscarContaFinanceiraUseCase buscarUseCase,
                                      DeletarContaFinanceiraUseCase deletarUseCase) {
        this.criarUseCase = criarUseCase;
        this.listarUseCase = listarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
    }

    @PostMapping
    public ResponseEntity<ContaFinanceiraResponseDTO> criar(@Valid @RequestBody ContaFinanceiraRequestDTO dto) {
        ContaFinanceira conta = new ContaFinanceira(
                dto.usuarioId(), dto.nome(),
                TipoConta.valueOf(dto.tipo()),
                dto.banco(), dto.saldoInicial(), dto.padrao()
        );
        ContaFinanceiraResponseDTO response = ContaFinanceiraResponseDTO.fromDomain(criarUseCase.executar(conta));
        return ResponseEntity.created(URI.create("/contas/" + response.id())).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaFinanceiraResponseDTO>> listarPorUsuario(@PathVariable UUID usuarioId) {
        List<ContaFinanceiraResponseDTO> response = listarUseCase.executar(usuarioId)
                .stream().map(ContaFinanceiraResponseDTO::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaFinanceiraResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ContaFinanceiraResponseDTO.fromDomain(buscarUseCase.executar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}

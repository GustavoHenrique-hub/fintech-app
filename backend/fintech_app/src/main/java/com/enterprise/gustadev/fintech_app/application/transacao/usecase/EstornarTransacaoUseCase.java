package com.enterprise.gustadev.fintech_app.application.transacao.usecase;

import com.enterprise.gustadev.fintech_app.domain.transacao.exception.TransacaoInvalidaException;
import com.enterprise.gustadev.fintech_app.domain.transacao.model.Transacao;
import com.enterprise.gustadev.fintech_app.domain.transacao.port.TransacaoRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class EstornarTransacaoUseCase {

    private final TransacaoRepositoryPort repository;

    public EstornarTransacaoUseCase(TransacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public ResponseEntity<Map<String,String>> executar(Long id, String code, Long userId, String userCode, Long contaId, String contaCode) {
        Map<String,String> response = new HashMap<>();
        Optional<Transacao> transacaoParaEditar = repository.buscarTransacao(id, code, userId, userCode, contaId, contaCode);

        try {
            if(transacaoParaEditar.isPresent()){
                Transacao transacao = transacaoParaEditar.get();
                transacao.
                transacao.setIndEstorno("");

                response.put("message", "Transação Estornada");
                return ResponseEntity.ok(response);
            }else {
                response.put("error", "Transação não encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }catch (TransacaoInvalidaException e){
            response.put("error", "Erro ao estornar transação" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
}

package com.enterprise.gustadev.fintech_app.domain.gasto.port;

import com.enterprise.gustadev.fintech_app.domain.gasto.model.Gasto;

import java.util.List;
import java.util.Optional;

public interface GastoRepositoryPort {

    Gasto salvar(Gasto gasto);

    List<Gasto> listarPorUsuario(Long usuarioId);

    Optional<Gasto> buscarPorId(Long id);

    void deletarPorId(Long id);
}

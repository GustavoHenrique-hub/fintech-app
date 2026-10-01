package com.enterprise.gustadev.fintech_app.domain.usuario.ports;

import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;

import java.util.Optional;

public interface CodigoVinculoTelegramRepositoryPort {

    CodigoVinculoTelegram salvar(CodigoVinculoTelegram codigo);

    Optional<CodigoVinculoTelegram> buscarPorCodigo(String codigo);

}

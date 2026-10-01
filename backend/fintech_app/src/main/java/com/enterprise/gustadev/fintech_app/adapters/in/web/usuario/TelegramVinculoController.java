package com.enterprise.gustadev.fintech_app.adapters.in.web.usuario;

import com.enterprise.gustadev.fintech_app.adapters.in.web.extrato.AutenticacaoCallbackN8n;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.CodigoVinculoTelegramResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.UsuarioTelegramResponseDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.usuario.dto.VincularTelegramRequestDTO;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.BuscarUsuarioPorTelegramUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.DesvincularTelegramUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.GerarCodigoVinculoTelegramUseCase;
import com.enterprise.gustadev.fintech_app.application.usuario.usecase.VincularTelegramUseCase;
import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.CodigoVinculoTelegram;
import com.enterprise.gustadev.fintech_app.domain.usuario.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Vínculo do chat do Telegram com a conta do app.
 *
 * <p>Fluxo: o app chama {@code POST /usuarios/me/telegram/codigo-vinculo} e abre o
 * deep link devolvido; o Telegram entrega {@code /start <codigo>} ao bot; o N8N chama
 * {@code POST /usuarios/telegram/vincular} com código + chat_id. Depois disso o bot
 * resolve o usuário por {@code GET /usuarios/telegram/{chatId}}.
 *
 * <p>As rotas {@code /usuarios/telegram/**} são da automação: não passam pelo
 * {@code SessaoTokenFilter} e autenticam por {@code X-Internal-Api-Key}.
 */
@Tag(name = "Integração Telegram", description = "Vínculo do chat do Telegram com a conta do usuário")
@RestController
@RequestMapping("/usuarios")
public class TelegramVinculoController {

    private final GerarCodigoVinculoTelegramUseCase gerarCodigoUseCase;
    private final VincularTelegramUseCase vincularUseCase;
    private final DesvincularTelegramUseCase desvincularUseCase;
    private final BuscarUsuarioPorTelegramUseCase buscarPorTelegramUseCase;
    private final AutenticacaoCallbackN8n autenticacao;
    private final String botUsername;

    public TelegramVinculoController(GerarCodigoVinculoTelegramUseCase gerarCodigoUseCase,
                                     VincularTelegramUseCase vincularUseCase,
                                     DesvincularTelegramUseCase desvincularUseCase,
                                     BuscarUsuarioPorTelegramUseCase buscarPorTelegramUseCase,
                                     AutenticacaoCallbackN8n autenticacao,
                                     @Value("${telegram.bot-username:}") String botUsername) {
        this.gerarCodigoUseCase = gerarCodigoUseCase;
        this.vincularUseCase = vincularUseCase;
        this.desvincularUseCase = desvincularUseCase;
        this.buscarPorTelegramUseCase = buscarPorTelegramUseCase;
        this.autenticacao = autenticacao;
        this.botUsername = botUsername == null ? "" : botUsername.replaceFirst("^@", "").trim();
    }

    // ── Chamadas do app (usuário logado) ─────────────────────────────────

    @Operation(summary = "Gerar código de vínculo",
            description = "Gera um código de uso único (10 min) e o deep link t.me/<bot>?start=<codigo>. " +
                    "Exige consentimento LGPD bot_telegram ativo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Código gerado"),
            @ApiResponse(responseCode = "400", description = "Consentimento bot_telegram ausente")
    })
    @PostMapping("/me/telegram/codigo-vinculo")
    public ResponseEntity<CodigoVinculoTelegramResponseDTO> gerarCodigo(
            @RequestAttribute("sessaoToken") SessaoToken sessao) {
        CodigoVinculoTelegram codigo = gerarCodigoUseCase.executar(sessao.getIdUsuario());
        return ResponseEntity.ok(CodigoVinculoTelegramResponseDTO.fromDomain(codigo, botUsername));
    }

    @Operation(summary = "Desvincular Telegram", description = "Remove o chat do Telegram da conta do usuário logado.")
    @ApiResponse(responseCode = "204", description = "Telegram desvinculado")
    @DeleteMapping("/me/telegram")
    public ResponseEntity<Void> desvincular(@RequestAttribute("sessaoToken") SessaoToken sessao) {
        desvincularUseCase.executar(sessao.getIdUsuario());
        return ResponseEntity.noContent().build();
    }

    // ── Chamadas da automação (N8N) ──────────────────────────────────────

    @Operation(summary = "Vincular chat (N8N)",
            description = "Consome o código recebido pelo bot e grava o chat_id no usuário. Autentica por X-Internal-Api-Key.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chat vinculado"),
            @ApiResponse(responseCode = "400", description = "Código inválido, expirado ou já usado"),
            @ApiResponse(responseCode = "401", description = "Chave interna inválida")
    })
    @PostMapping("/telegram/vincular")
    public ResponseEntity<UsuarioTelegramResponseDTO> vincular(
            @RequestHeader(value = "X-Internal-Api-Key", required = false) String chaveInterna,
            @Valid @RequestBody VincularTelegramRequestDTO dto) {
        autenticacao.exigirChaveInterna(chaveInterna);
        Usuario usuario = vincularUseCase.executar(dto.codigo(), dto.chatId());
        return ResponseEntity.ok(UsuarioTelegramResponseDTO.of(usuario, null));
    }

    @Operation(summary = "Resolver usuário pelo chat (N8N)",
            description = "Devolve o usuário ligado ao chat_id, desde que o consentimento bot_telegram esteja ativo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "401", description = "Chave interna inválida"),
            @ApiResponse(responseCode = "404", description = "Chat não vinculado ou sem consentimento")
    })
    @GetMapping("/telegram/{chatId}")
    public ResponseEntity<UsuarioTelegramResponseDTO> buscarPorChat(
            @RequestHeader(value = "X-Internal-Api-Key", required = false) String chaveInterna,
            @Parameter(description = "chat.id do Telegram") @PathVariable Long chatId) {
        autenticacao.exigirChaveInterna(chaveInterna);
        BuscarUsuarioPorTelegramUseCase.Resultado resultado = buscarPorTelegramUseCase.executar(chatId);
        return ResponseEntity.ok(UsuarioTelegramResponseDTO.of(resultado.usuario(), resultado.contaPadraoId()));
    }
}

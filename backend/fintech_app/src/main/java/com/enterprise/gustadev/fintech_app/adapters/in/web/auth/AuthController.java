package com.enterprise.gustadev.fintech_app.adapters.in.web.auth;

import com.enterprise.gustadev.fintech_app.adapters.in.web.auth.dto.LoginRequestDTO;
import com.enterprise.gustadev.fintech_app.adapters.in.web.auth.dto.LoginResponseDTO;
import com.enterprise.gustadev.fintech_app.application.auth.usecase.LoginUseCase;
import com.enterprise.gustadev.fintech_app.application.auth.usecase.LogoutUseCase;
import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Login e logout de usuários")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(LoginUseCase loginUseCase, LogoutUseCase logoutUseCase) {
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @Operation(summary = "Login", description = "Autentica o usuário com email e senha e retorna um token de sessão válido por 30 dias.")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        SessaoToken sessao = loginUseCase.executar(dto.email(), dto.senha());
        return ResponseEntity.ok(LoginResponseDTO.fromDomain(sessao));
    }

    @Operation(summary = "Logout", description = "Invalida o token de sessão. Envie o token no header: Authorization: Bearer <token>")
    @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "").trim();
        logoutUseCase.executar(token);
        return ResponseEntity.noContent().build();
    }
}

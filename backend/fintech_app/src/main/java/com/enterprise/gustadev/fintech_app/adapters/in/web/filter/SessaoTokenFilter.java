package com.enterprise.gustadev.fintech_app.adapters.in.web.filter;

import com.enterprise.gustadev.fintech_app.domain.auth.model.SessaoToken;
import com.enterprise.gustadev.fintech_app.domain.auth.port.SessaoTokenRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SessaoTokenFilter extends OncePerRequestFilter {

    private final SessaoTokenRepositoryPort sessaoRepository;

    public SessaoTokenFilter(SessaoTokenRepositoryPort sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return path.startsWith("/auth/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator")
                || (path.equals("/usuarios") && "POST".equalsIgnoreCase(method));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Token de autenticacao necessario");
            return;
        }

        String token = authHeader.substring(7).trim();
        Optional<SessaoToken> sessao = sessaoRepository.buscarPorToken(token);

        if (sessao.isEmpty() || sessao.get().expirou()) {
            sendUnauthorized(response, "Token invalido ou expirado");
            return;
        }

        request.setAttribute("sessaoToken", sessao.get());
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String mensagem) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + mensagem + "\"}");
    }
}

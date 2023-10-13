package com.gollajo.global;

import com.gollajo.domain.jwt.TokenProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_LIST = List.of(
            "/test",
            "/auth/kakao/callback"
    );

    private final TokenProcessor tokenProcessor;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProcessor.resolveToken(request);
        tokenProcessor.validateToken(token);
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return ALLOWED_LIST.stream()
                .anyMatch(url -> request.getRequestURI().contains(url));
    }
}

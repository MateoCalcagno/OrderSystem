package ordersystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Si no hay token, seguir normal
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer token
        String token = authHeader.substring(7);

        // 3. Extraer username y role desde JWT
        String username = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        // 4. Si no hay autenticación previa
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Crear autenticación SIN ir a la DB
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. Guardar en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 7. Continuar la cadena
        filterChain.doFilter(request, response);
    }
}
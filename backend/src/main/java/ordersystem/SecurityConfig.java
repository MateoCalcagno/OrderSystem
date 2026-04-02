package ordersystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) 
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/register").permitAll()                // Registro libre
                .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("USER", "ADMIN") // GET productos
                .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")          // Crear producto solo admin
                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")           // Modificar producto solo admin
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")        // Borrar producto solo admin
                .requestMatchers(HttpMethod.POST, "/orders/**").hasRole("USER")             // Crear pedido
                .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("USER", "ADMIN")  // Ver pedidos
                .anyRequest().authenticated()                                              // Todo lo demás requiere autenticación
            )
            .httpBasic(withDefaults()); 
            
        return http.build();
    }

    // Este bloque le da permiso a React (puerto 3000)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Origen permitido
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Permite mandar credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
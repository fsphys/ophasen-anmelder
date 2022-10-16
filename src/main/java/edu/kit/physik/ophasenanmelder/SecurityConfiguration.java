package edu.kit.physik.ophasenanmelder;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        http.httpBasic();

        http.authorizeExchange()

                .pathMatchers(HttpMethod.GET, "/", "/swagger-ui", "/webjars/**", "/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.HEAD, "/", "/swagger-ui", "/webjars/**", "/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/", "/swagger-ui", "/webjars/**", "/v3/api-docs/**").permitAll()

                .pathMatchers(HttpMethod.GET, "/event/participation").authenticated()
                .pathMatchers(HttpMethod.GET, "/event/draw/participation").authenticated()
                .pathMatchers(HttpMethod.GET, "/event/type/{id}/draw").authenticated()

                .pathMatchers(HttpMethod.GET, "/event/**").permitAll()
                .pathMatchers(HttpMethod.HEAD, "/event/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/event/**").permitAll()

                .pathMatchers(HttpMethod.POST, "/event/participation").permitAll()
                .pathMatchers(HttpMethod.DELETE, "/event/participation/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/event/participation/**").permitAll()

                .pathMatchers("/event/**").authenticated()

                .pathMatchers(HttpMethod.GET, "/actuator/health").permitAll()

                .anyExchange().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)));

        return http.build();
    }

    @Bean
    MapReactiveUserDetailsService mapReactiveUserDetailsService(final SecurityProperties properties) {
        return new MapReactiveUserDetailsService(User.withUsername(properties.getUser().getName()).password(properties.getUser().getPassword()).roles("USER").build());
    }
}

package com.archpatterns.deliveryordermanager.configuration;

import com.archpatterns.deliveryordermanager.service.JwtAuthorizationFilter;
import com.archpatterns.deliveryordermanager.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.archpatterns.deliveryordermanager.enums.TipoRole.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {

    private static final String ROLE_PREFIX = "ROLE_";

    private static final String[] WHITELIST = {
            "/deliveryorder-manager/api/orders",
            "/deliveryorder-manager/api/orders/",
            "/deliveryorder-manager/api/orders**",
            "/deliveryorder-manager/api/orders/buyer/*",
            "/deliveryorder-manager/api/orders/deliver/*",
            "/manage/health",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService,
                                           AuthenticationConfiguration config) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
        .requestMatchers(WHITELIST).permitAll() // Esto permite GET, POST, PATCH, etc. en esos paths
        //ASI TIENEN QUE SER CON AUTENTICACION
        //.requestMatchers(PATCH, "/deliveryorder-manager/api/orders/{delivery_order_id}/pick").hasAnyAuthority(ROLE_PREFIX + DELIVERY.name(), ROLE_PREFIX + ADMIN.name())
        //.requestMatchers(PATCH, "/deliveryorder-manager/api/orders/{delivery_order_id}/deliver").hasAnyAuthority(ROLE_PREFIX + DELIVERY.name(), ROLE_PREFIX + ADMIN.name())
        //.requestMatchers(PATCH, "/deliveryorder-manager/api/orders/{delivery_order_id}/cancel").hasAnyAuthority(ROLE_PREFIX + DELIVERY.name(), ROLE_PREFIX + ADMIN.name())
        //.requestMatchers(POST, "/deliveryorder-manager/api/orders/{delivery_order_id}/qualify").hasAnyAuthority(ROLE_PREFIX + BUYER.name(), ROLE_PREFIX + ADMIN.name())

        //SIN AUTENTICACION PRUEBA TEMPORAL
        .requestMatchers(PATCH, "/deliveryorder-manager/api/orders/{delivery_order_id}/pick").permitAll()
        .requestMatchers(PATCH, "/deliveryorder-manager/api/orders/*/pick").permitAll()
        .requestMatchers(PATCH, "/deliveryorder-manager/api/orders/{delivery_order_id}/deliver").permitAll()
        .requestMatchers(PATCH, "/deliveryorder-manager/api/orders/{delivery_order_id}/cancel").permitAll()
        .requestMatchers(POST, "/deliveryorder-manager/api/orders/{delivery_order_id}/qualify").permitAll()

        .anyRequest().authenticated()
)

                .addFilterAfter(new JwtAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(STATELESS);

        return http.build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(AuthenticationConfiguration config) throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager(config));
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

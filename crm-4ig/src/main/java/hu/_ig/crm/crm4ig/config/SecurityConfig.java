package hu._ig.crm.crm4ig.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import hu._ig.crm.crm4ig.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import static hu._ig.crm.crm4ig.constants.Constants.ROLES;
import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final CsrfConfig csrfConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.security.keyFile}")
    private String keyFile;

    @Value("${spring.security.password}")
    private String password;

    @Value("${spring.security.alias}")
    private String alias;

    @Value("${spring.security.providerUrl}")
    private String providerUrl;

    @Value("${spring.security.token-timeout}")
    private String tokenTimeout;

    @Value("${spring.security.client-secret}")
    private String clientSecret;

    @Value("${angular.client.url}")
    private String angularClientUrl;

    public SecurityConfig(CorsConfig corsConfig, CsrfConfig csrfConfig, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.corsConfig = corsConfig;
        this.csrfConfig = csrfConfig;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/oauth2/**")
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/oauth2/**").authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfConfig.csrfTokenRepository())
                        .csrfTokenRequestHandler(csrfConfig.csrfTokenRequestHandler())
                        .ignoringRequestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/webjars/swagger-ui/index.html",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html"
                        )
                )
//                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JWKSource<SecurityContext> jwkSource()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        JWKSet jwkSet = buildJWKSet();
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private JWKSet buildJWKSet() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream fis = this.getClass().getClassLoader().getResourceAsStream(keyFile);) {
            keyStore.load(fis, password.toCharArray());
            return JWKSet.load(keyStore, name -> password.toCharArray());
        }
    }

    @Bean
    AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().issuer(providerUrl).build();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registredClient = RegisteredClient.withId("crm4ig").clientId("crm4igclientapp")
                .clientSecret(passwordEncoder.encode(clientSecret))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(angularClientUrl).scope("read").scope("write")
                .tokenSettings(tokenSettings()).build();
        return new InMemoryRegisteredClientRepository(registredClient);
    }

    @Bean
    TokenSettings tokenSettings() {
        return TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(Long.parseLong(tokenTimeout))).build();
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                Authentication principal = context.getPrincipal();
                Set<String> authorities = principal.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                context.getClaims().claim(ROLES, authorities);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

}
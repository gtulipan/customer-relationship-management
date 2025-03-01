package hu._ig.crm.crm4ig.config;

import hu._ig.crm.crm4ig.filter.CsrfCookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class CsrfConfig {

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setHeaderName("XSRF-TOKEN");
        csrfTokenRepository.setParameterName("_csrf");
        return csrfTokenRepository;
    }

    @Bean
    public CsrfTokenRequestHandler csrfTokenRequestHandler() {
        return new CustomCsrfTokenRequestHandler();
    }

    @Bean
    public OncePerRequestFilter csrfCookieFilter() {
        return new CsrfCookieFilter(csrfTokenRepository());
    }
}

package hu._ig.crm.crm4ig.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;

import java.util.function.Supplier;

public class CustomCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfTokenSupplier) {
        CsrfToken csrfToken = csrfTokenSupplier.get();
        if (csrfToken != null) {
            response.addHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        return csrfToken != null ? csrfToken.getToken() : null;
    }
}
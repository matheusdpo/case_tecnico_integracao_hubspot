package br.com.meetime.hubspot.v1.config;

import br.com.meetime.hubspot.v1.service.RateLimitService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitConfig implements Filter {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = httpRequest.getRemoteAddr();
        String requestUri = httpRequest.getRequestURI();

        String rateLimitKey = clientIp + ":" + requestUri;

        if (!rateLimitService.tryConsume(rateLimitKey)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("""
                {
                    "error": "Too many requests",
                    "message": "Rate limit exceeded - 100 requests per 10 seconds or 250,000 per day"
                }
                """);
            return;
        }

        chain.doFilter(request, response);
    }
}
package com.bank.fdsimulator.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        
        logger.info("🔥 REQUEST: {} {}", method, uri);
        
        chain.doFilter(request, response);
        
        logger.info("✅ RESPONSE: {} {} - Status: {}", method, uri, 
                   ((jakarta.servlet.http.HttpServletResponse)response).getStatus());
    }
}

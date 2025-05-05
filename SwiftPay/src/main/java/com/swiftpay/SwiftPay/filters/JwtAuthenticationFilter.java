package com.swiftpay.SwiftPay.filters;

import com.swiftpay.SwiftPay.Exception.InvalidTokenException;
import com.swiftpay.SwiftPay.Exception.TokenExpiredException;
import com.swiftpay.SwiftPay.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request ,
                                    HttpServletResponse response ,
                                    FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // ✅ 1. First, allow /api/auth/** without checking token
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return; // ✅ Exit the filter early for login/register
        }

        // ✅ 2. Check token for other APIs
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Remove 'Bearer ' prefix
            try{
                username = JwtUtil.validateToken(jwtToken);
            }catch (ExpiredJwtException e){
                throw new TokenExpiredException("JWT token expired. Please login again or Use refresh token.");
            }catch (JwtException e){
                throw new InvalidTokenException("The provided token is Invalid/Wrong");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            String role = JwtUtil.extractUserRole(jwtToken); // extract role from token
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username, // principal
                            null,     // credentials
                            authorities // authorities (empty for now)->now authorities are not empty
                    );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set authenticated user to context
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request , response);
    }

}

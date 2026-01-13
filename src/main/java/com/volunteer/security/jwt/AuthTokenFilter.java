package com.volunteer.security.jwt;

import com.volunteer.entity.Volunteer;
import com.volunteer.repository.VolunteerRepository;
import com.volunteer.security.services.UserDetailsServiceImple;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private UserDetailsServiceImple  userDetailsServiceImple;
    private final JwtUtils jwtUtils;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImple userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImple = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO: Implement JWT token validation and authentication
        logger.info("AuthTokenFilter: Filtering request for JWT authentication");
        try {
            String jwt = parseJwt(request);
            logger.info("AuthTokenFilter: JWT received {}", jwt);
            if (jwt != null) {
                Claims decoded = jwtUtils.decodeJwt(jwt);
                String email = decoded.getSubject();
                logger.info("AuthTokenFilter: Found JWT for username {}", email);

                UserDetails userDetails = userDetailsServiceImple.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        logger.info("AuthTokenFilter: Filtering completed");
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        logger.info("AuthTokenFilter: Parsing JWT header");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
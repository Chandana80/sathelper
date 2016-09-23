package com.ojass.sathelper.config;

import com.ojass.sathelper.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chandana on 9/21/2016.
 */
public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        UsernamePasswordAuthenticationToken auth = extractAuthentication(httpRequest, httpResponse);
        SecurityContextHolder.getContext().setAuthentication(auth);


        filterChain.doFilter(request, response);


    }


    private UsernamePasswordAuthenticationToken extractAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String authValue = ((HttpServletRequest) request).getHeader("Authorization");
        String token = null;
        if (authValue != null && authValue.indexOf("Bearer ") > -1) {
            token = authValue.split("Bearer ")[1];

            //TODO: DECODE in PRODUCTION or use a SSO Solution or refactor: Current test format email=role1~role2~
            String userId = token.split("=")[0];
            String[] roles = token.split("=")[1].split("~");
            Set<Role> roleSet = new HashSet<Role>();

            for (String role : roles) {
                Role r = new Role();
                r.setName(role);
                roleSet.add(r);
            }

            return new UsernamePasswordAuthenticationToken(userId, "NoPass", roleSet);
        }
        return null;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        return extractAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        StringBuilder roles = new StringBuilder();
        for (GrantedAuthority r : authResult.getAuthorities()) {
            roles.append(r.getAuthority()).append("~");
        }
        String token = authResult.getPrincipal() + "=" + roles.toString();
        response.addHeader("Authorization", "Bearer " + token.toString());
    }
}

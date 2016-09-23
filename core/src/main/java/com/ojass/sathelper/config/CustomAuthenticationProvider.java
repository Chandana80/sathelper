package com.ojass.sathelper.config;

import com.ojass.sathelper.entity.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @PersistenceContext
    private EntityManager em;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = null;
        try {
            user = (User) em.createQuery("Select u from User u where u.email = :email").setParameter("email", username).getSingleResult();


        } catch(Exception ex) {
            throw new BadCredentialsException("Username not found.");

        }
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        Collection<? extends GrantedAuthority> authorities = user.getRoles();

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    public boolean supports(Class<?> arg0) {
        return true;
    }

}
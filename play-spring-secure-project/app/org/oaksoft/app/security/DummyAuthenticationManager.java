package org.oaksoft.app.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class DummyAuthenticationManager implements AuthenticationManager
{
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), list);
    }
}

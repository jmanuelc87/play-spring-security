package org.oaksoft.security.web.authentication;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import play.mvc.Http;

import java.io.IOException;

/**
 * @author Juan Manuel Carballo
 * @version 1.0
 */
public class UsernamePasswordPlayAuthenticationFilter extends AbstractPlayAuthenticationProcessingFilter
{
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "play_auth_username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "play_auth_password";


    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    @Override
    public Authentication attemptAuthentication(Http.Context context) throws AuthenticationException, IOException
    {
        if (!context.request().method().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + context.request().method());
        }

        String username = obtainUsername(context);
        String password = obtainPassword(context);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainUsername(Http.Context context)
    {
        return context.request().body().asFormUrlEncoded().get(usernameParameter)[0];
    }

    protected String obtainPassword(Http.Context context)
    {
        return context.request().body().asFormUrlEncoded().get(passwordParameter)[0];
    }

    public void setUsernameParameter(String usernameParameter)
    {
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter)
    {
        this.passwordParameter = passwordParameter;
    }
}

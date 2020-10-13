package br.com.casadocodigo.integracao.bookserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class ConfiguracaoResource {

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private ClientTokenServices clientTokenServices;

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(){
        OAuth2ProtectedResourceDetails resourceDetails = bookserver();
        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, oAuth2ClientContext);
        AccessTokenProviderChain providerChain = new AccessTokenProviderChain(
                Arrays.asList(new AuthorizationCodeAccessTokenProvider())
        );
        providerChain.setClientTokenServices(clientTokenServices);
        template.setAccessTokenProvider(providerChain);
        return template;
    }

    @Bean
    public OAuth2ProtectedResourceDetails bookserver(){
        AuthorizationCodeResourceDetails authorizationCodeResourceDetails = new AuthorizationCodeResourceDetails();
        authorizationCodeResourceDetails.setId("bookserver");
        authorizationCodeResourceDetails.setTokenName("oauth_token");
        authorizationCodeResourceDetails.setClientId("cliente-app");
        authorizationCodeResourceDetails.setClientSecret("123456");
        authorizationCodeResourceDetails.setAccessTokenUri("http://localhost:8080/oauth/token");
        authorizationCodeResourceDetails.setUserAuthorizationUri("http://localhost:8080/oauth/authorize");
        authorizationCodeResourceDetails.setScope(Arrays.asList("read", "write"));
        authorizationCodeResourceDetails.setPreEstablishedRedirectUri("http://localhost:9000/integracao/callback");
        authorizationCodeResourceDetails.setUseCurrentUri(false);
        authorizationCodeResourceDetails.setClientAuthenticationScheme(AuthenticationScheme.header);

        return authorizationCodeResourceDetails;
    }
}

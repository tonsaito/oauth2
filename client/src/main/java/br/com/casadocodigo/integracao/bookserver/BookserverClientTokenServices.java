package br.com.casadocodigo.integracao.bookserver;

import br.com.casadocodigo.configuracao.seguranca.UsuarioLogado;
import br.com.casadocodigo.usuarios.AcessoBookserver;
import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class BookserverClientTokenServices implements ClientTokenServices {

    @Autowired
    private UsuariosRepository repository;

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, Authentication authentication) {
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();

        Usuario usuario = repository.findById(usuarioLogado.getId());
        String accessToken = usuario.getAcessoBookserver().getAcessoToken();
        String refreshToken = usuario.getAcessoBookserver().getRefreshToken();
        Calendar dataDeExpiracao = usuario.getAcessoBookserver()
                .getDataDeExpiracao();


        if(accessToken == null){
            return null;
        }

        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
//        DefaultOAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(refreshToken);
//        oAuth2AccessToken.setRefreshToken(oAuth2RefreshToken);
        oAuth2AccessToken.setExpiration(dataDeExpiracao.getTime());


        return oAuth2AccessToken;
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, Authentication authentication, OAuth2AccessToken oAuth2AccessToken) {
        AcessoBookserver acessoBookserver = new AcessoBookserver();
        acessoBookserver.setAcessoToken(oAuth2AccessToken.getValue());

        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(oAuth2AccessToken.getExpiration());

        acessoBookserver.setDataDeExpiracao(expirationDate);
        if(oAuth2AccessToken.getRefreshToken() != null){
            acessoBookserver.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
        }

        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        Usuario usuario = repository.findById(usuarioLogado.getId());

        usuario.setAcessoBookserver(acessoBookserver);

        repository.save(usuario);
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, Authentication authentication) {
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        Usuario usuario = repository.findById(usuarioLogado.getId());

        usuario.setAcessoBookserver(null);
        repository.save(usuario);
    }
}

package br.com.casadocodigo.configuracao.database;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class ConfiguracaoOAuthDB {

    @Bean(name = "dsOauth")
    @ConfigurationProperties(prefix = "spring.ds-oauth")
    public DataSource oauthDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dsBookserver")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource bookserverDataSource() {
        return DataSourceBuilder.create().build();
    }

}
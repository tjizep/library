package org.booklibrary.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${openapi.openAPILibrary.base-path:/v1}")
    private String apiBasePath;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
        /*http.requestMatchers()
                .antMatchers(apiBasePath + "/nbook")
                .antMatchers(apiBasePath + "/nbook/findByStatus")
                .antMatchers(HttpMethod.POST, apiBasePath + "/book/**")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().access("#oauth2.hasScope('write:books') and #oauth2.hasScope('read:books')");*/
    }
}
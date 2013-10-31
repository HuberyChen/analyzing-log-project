package com.quidsi.log.analyzing;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.quidsi.core.platform.DefaultSiteWebConfig;
import com.quidsi.core.platform.web.DeploymentSettings;
import com.quidsi.core.platform.web.site.SiteSettings;
import com.quidsi.core.platform.web.site.session.SessionProviderType;
import com.quidsi.log.analyzing.web.interceptor.LoginRequiredInterceptor;

/**
 * @author neo
 */
@Configuration
public class WebConfig extends DefaultSiteWebConfig {

    @Inject
    Environment env;

    @Inject
    ServletContext servletContext;

    @Bean
    public DeploymentSettings deploymentSettings() {
        DeploymentSettings settings = super.deploymentSettings();
        settings.setHTTPPort(env.getRequiredProperty("site.httpPort", int.class));
        settings.setHTTPSPort(env.getRequiredProperty("site.httpsPort", int.class));
        settings.setDeploymentContext(env.getProperty("site.deploymentContext"), servletContext);
        return settings;
    }

    @Override
    public SiteSettings siteSettings() {
        SiteSettings settings = new SiteSettings();
        settings.setErrorPage("/error");
        settings.setResourceNotFoundPage("forward:/error/resource-not-found");
        settings.setSessionTimeOutPage("redirect:/home");
        settings.setSessionProviderType(env.getProperty("site.sessionProvider", SessionProviderType.class, SessionProviderType.LOCAL));
        settings.setRemoteSessionServers(env.getProperty("site.remoteSessionServers"));
        settings.setJSDir("/dstatic/js");
        settings.setCSSDir("/dstatic/css");
        return settings;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // on server env, /mstatic will be handled by apache or CDN, this only
        // apply to local dev
        registry.addResourceHandler("/dstatic/**").addResourceLocations("/dstatic/");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Inject
    EntityManagerFactory entityManagerFactory;

    @Bean
    public LoginRequiredInterceptor loginRequiredInterceptor() {
        return new LoginRequiredInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor());
        registry.addInterceptor(requestContextInterceptor());
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory);
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor);
        registry.addInterceptor(cookieInterceptor());
        registry.addInterceptor(sessionInterceptor());
        registry.addInterceptor(loginRequiredInterceptor());
    }
}

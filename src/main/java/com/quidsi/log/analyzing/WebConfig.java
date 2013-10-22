package com.quidsi.log.analyzing;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.quidsi.core.platform.DefaultServiceWebConfig;
import com.quidsi.core.platform.web.DeploymentSettings;

/**
 * @author neo
 */
@Configuration
public class WebConfig extends DefaultServiceWebConfig {

    @Inject
    Environment env;

    @Inject
    EntityManagerFactory entityManagerFactory;

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
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor());
        registry.addInterceptor(requestContextInterceptor());
        registry.addInterceptor(trackInterceptor());
        OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
        interceptor.setEntityManagerFactory(entityManagerFactory);
        registry.addWebRequestInterceptor(interceptor);
    }
}

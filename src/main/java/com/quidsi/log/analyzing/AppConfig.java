package com.quidsi.log.analyzing;

import com.quidsi.core.crypto.EncryptionUtils;
import com.quidsi.core.database.ConnectionPoolDataSource;
import com.quidsi.core.database.JDBCAccess;
import com.quidsi.core.database.JPAAccess;
import com.quidsi.core.platform.DefaultAppConfig;
import com.quidsi.core.platform.PlatformScopeResolver;
import com.quidsi.core.platform.runtime.RuntimeEnvironment;
import com.quidsi.core.platform.runtime.RuntimeSettings;
import com.quidsi.core.platform.scheduler.SchedulerImpl;
import com.quidsi.core.util.ClasspathResource;
import com.quidsi.log.analyzing.scheduler.service.SchedulerSettings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;

import java.sql.Connection;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackageClasses = AppConfig.class, scopeResolver = PlatformScopeResolver.class)
public class AppConfig extends DefaultAppConfig {
    @Inject
    Environment env;

    @Bean
    public RuntimeSettings runtimeSettings() {
        RuntimeSettings settings = super.runtimeSettings();
        settings.setEnvironment(env.getProperty("site.environment", RuntimeEnvironment.class, RuntimeEnvironment.PROD));
        return settings;
    }

    @Bean
    public DataSource dataSource() {
        ConnectionPoolDataSource dataSource = new ConnectionPoolDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(EncryptionUtils.decrypt(env.getRequiredProperty("jdbc.password"), new ClasspathResource("private.key")));
        dataSource.setValidationQuery("select 1");
        dataSource.setDatabaseName("APILogDB");
        dataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(AppConfig.class.getPackage().getName());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServer2008Dialect");
        vendorAdapter.setShowSql(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public JPAAccess jpaAccess() {
        return new JPAAccess();
    }

    @Bean
    public JDBCAccess jdbcAccess() {
        JDBCAccess jdbcAccess = new JDBCAccess();
        jdbcAccess.setDataSource(dataSource());
        return jdbcAccess;
    }

    @Bean
    SchedulerImpl scheduler() {
        return new SchedulerImpl();
    }

    @Bean
    public SchedulerSettings schedulerSettings() {
        SchedulerSettings settings = new SchedulerSettings();
        settings.setLogAnalyzedCron(env.getRequiredProperty("log.analyzed.cron"));
        return settings;
    }
}
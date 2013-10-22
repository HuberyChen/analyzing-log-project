package com.quidsi.log.analyzing;

import java.io.File;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * @author neo
 */
public class ConfigurationTest extends SpringTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    public void contextShouldBeInitialized() {
        Assert.assertNotNull(applicationContext);
    }

    @Test
    public void verifyBeanConfiguration() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            applicationContext.getBean(beanName);
        }
    }

    @Test
    public void test() {
        File file = new File("D:\\test");
        System.out.println(file.getAbsolutePath());
    }
}

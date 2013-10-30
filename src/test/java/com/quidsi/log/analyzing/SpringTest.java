package com.quidsi.log.analyzing;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author neo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TestEnvironmentConfig.class })
@TransactionConfiguration
@ActiveProfiles("test")
@WebAppConfiguration
public abstract class SpringTest {

}

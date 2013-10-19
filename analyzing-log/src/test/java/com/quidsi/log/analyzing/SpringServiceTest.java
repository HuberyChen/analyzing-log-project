package com.quidsi.log.analyzing;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.quidsi.core.platform.web.filter.PlatformFilter;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TestEnvironmentConfig.class })
@TransactionConfiguration
@WebAppConfiguration
public abstract class SpringServiceTest {

    @Inject
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void createMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(new PlatformFilter()).build();
    }
}

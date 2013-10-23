package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import com.quidsi.log.analyzing.SpringServiceTest;

public class ActionLogDetailControllerTest extends SpringServiceTest {

    @Test
    public void scanActionLogDetailTest() throws Exception {
        mockMvc.perform(get("/project/instance/log/detail")).andExpect(status().isOk());
    }

}

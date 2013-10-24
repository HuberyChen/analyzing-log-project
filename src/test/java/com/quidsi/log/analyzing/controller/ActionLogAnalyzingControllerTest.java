package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.log.analyzing.SpringServiceTest;

public class ActionLogAnalyzingControllerTest extends SpringServiceTest {

    @Test
    public void actionLogdAnalyzingTest() throws Exception {
        mockMvc.perform(get("/project/instance/log/action").content(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("root", "D:\\test")).andExpect(status().isOk());
    }

}

package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.log.analyzing.SpringServiceTest;

public class LogFileControllerTest extends SpringServiceTest {

    @Test
    public void scanServerLogTest() throws Exception {
        mockMvc.perform(get("/project/instance/log").content(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("root", "D:\\test")).andExpect(status().isOk());
    }

}

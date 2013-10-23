package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.log.analyzing.SpringServiceTest;

public class ProjectControllerTest extends SpringServiceTest {

    @Test
    public void scanProjectTest() throws Exception {
        mockMvc.perform(get("/project").content(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("root", "D:\\test")).andExpect(status().isOk());
    }

    @Test
    public void scanServerTest() throws Exception {
        mockMvc.perform(get("/project/instance").content(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("root", "D:\\test")).andExpect(status().isOk());
    }

}

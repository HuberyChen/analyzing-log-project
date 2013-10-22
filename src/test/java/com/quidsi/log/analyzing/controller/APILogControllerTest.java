package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.log.analyzing.SpringServiceTest;

public class APILogControllerTest extends SpringServiceTest {

    @Test
    public void scanAPIHostLogTest() throws Exception {
        mockMvc.perform(get("/api/host/log").content(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("root", "D:\\test")).andExpect(status().isOk());
    }

}

package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.log.analyzing.SpringServiceTest;

public class LogRecordTest extends SpringServiceTest {

    @Test
    public void logRecordTest() throws Exception {
        mockMvc.perform(get("/log/path").content(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("SCAN_FOLDER", "/Prod-gcsvc1")).andExpect(status().isOk());
    }
}

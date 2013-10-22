package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.core.json.JSONBinder;
import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.SpringServiceTest;
import com.quidsi.log.analyzing.request.ActionLogRequest;

public class LogRecordTest extends SpringServiceTest {

    @Test
    public void logRecordTest() throws Exception {
        ActionLogRequest request = new ActionLogRequest();
        request.setLogDate(DateUtils.date(2013, 10, 18));
        request.setPath("D:\\test");
        mockMvc.perform(get("/log/path").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(JSONBinder.binder(ActionLogRequest.class).toJSON(request))).andExpect(
                status().isOk());
    }

    @Test
    public void actionLogReadTest() throws Exception {
        ActionLogRequest request = new ActionLogRequest();
        request.setLogDate(DateUtils.date(2013, 10, 18));
        request.setPath("D:\\log");
        mockMvc.perform(get("/log/path").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(JSONBinder.binder(ActionLogRequest.class).toJSON(request))).andExpect(
                status().isOk());
    }
}

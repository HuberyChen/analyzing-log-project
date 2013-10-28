package com.quidsi.log.analyzing.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.quidsi.core.json.JSONBinder;
import com.quidsi.log.analyzing.SpringServiceTest;
import com.quidsi.log.analyzing.request.ActionLogAnalyzingRequest;

public class ActionLogAnalyzingControllerTest extends SpringServiceTest {

    @Test
    public void actionLogAnalyzingTest() throws Exception {
        ActionLogAnalyzingRequest request = new ActionLogAnalyzingRequest();
        request.setProjectName("giftco-service");
        request.setServerName("Prod-gcsvc1");
        mockMvc.perform(post("/project/instance/log/action").contentType(MediaType.APPLICATION_JSON).content(JSONBinder.binder(ActionLogAnalyzingRequest.class).toJSON(request))).andExpect(
                status().isOk());
    }
}

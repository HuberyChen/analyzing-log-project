package com.quidsi.log.analyzing.utils;

import org.junit.Test;

/**
 * @author hubery.chen
 */
public class LogReadUtilsTest {

    @Test
    public void logReadTest() {
        String msg = LogReadUtils.logRead("D:\\test\\giftco-service\\Prod-gcsvc1\\2013\\10\\19\\action\\decompression\\giftco-service-action.2013-11-02_05.log");
        System.out.println(msg);
    }
}

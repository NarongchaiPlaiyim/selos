package com.clevel.selos.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class UtilTest {
    private static Logger log = LoggerFactory.getLogger(UtilTest.class);

    public UtilTest() {
    }

    @Test
    public void testReplaceToBlank() throws Exception {
        log.info("testReplaceToBlank.");
        String strToReplace = "เขต|แขวง";
        String text1 = "เขตลาดพร้าว";
        String text2 = "แขวงดุสิต";

        log.info("text1 output: {}",Util.replaceToBlank(text1,strToReplace));
        log.info("text2 output: {}",Util.replaceToBlank(text2,strToReplace));

        assertEquals("expected equal.", "ลาดพร้าว", Util.replaceToBlank(text1,strToReplace));
        assertEquals("expected equal.", "ดุสิต", Util.replaceToBlank(text2,strToReplace));
        assertEquals("expected equal.", "", Util.replaceToBlank(null,strToReplace));
        assertEquals("expected equal.", "เขตลาดพร้าว", Util.replaceToBlank(text1, null));
        assertEquals("expected equal.", "", Util.replaceToBlank(null,null));
    }
}

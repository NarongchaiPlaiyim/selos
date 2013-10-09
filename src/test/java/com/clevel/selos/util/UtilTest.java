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
        String strToReplace = "‡¢µ|·¢«ß";
        String text1 = "‡¢µ¥ÿ ‘µ";
        String text2 = "·¢«ß¥ÿ ‘µ";

        log.info("text1 output: {}",Util.replaceToBlank(text1,strToReplace));
        log.info("text2 output: {}",Util.replaceToBlank(text2,strToReplace));

        assertEquals("expected equal.", "¥ÿ ‘µ", Util.replaceToBlank(text1,strToReplace));
        assertEquals("expected equal.", "¥ÿ ‘µ", Util.replaceToBlank(text2,strToReplace));
        assertEquals("expected equal.", "", Util.replaceToBlank(null,strToReplace));
        assertEquals("expected equal.", "‡¢µ¥ÿ ‘µ", Util.replaceToBlank(text1, null));
        assertEquals("expected equal.", "", Util.replaceToBlank(null,null));
    }
}

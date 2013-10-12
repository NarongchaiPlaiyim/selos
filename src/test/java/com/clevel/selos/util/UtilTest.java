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
        String strToReplace = "ࢵ|�ǧ";
        String text1 = "ࢵ�Ҵ�����";
        String text2 = "�ǧ���Ե";

        log.info("text1 output: {}",Util.replaceToBlank(text1,strToReplace));
        log.info("text2 output: {}",Util.replaceToBlank(text2,strToReplace));

        assertEquals("expected equal.", "�Ҵ�����", Util.replaceToBlank(text1,strToReplace));
        assertEquals("expected equal.", "���Ե", Util.replaceToBlank(text2,strToReplace));
        assertEquals("expected equal.", "", Util.replaceToBlank(null,strToReplace));
        assertEquals("expected equal.", "ࢵ�Ҵ�����", Util.replaceToBlank(text1, null));
        assertEquals("expected equal.", "", Util.replaceToBlank(null,null));
    }
}

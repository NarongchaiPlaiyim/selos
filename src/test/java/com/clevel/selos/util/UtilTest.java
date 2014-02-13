package com.clevel.selos.util;

import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {
//    private static Logger log = LoggerFactory.getLogger(UtilTest.class);

    public UtilTest() {
    }

//    @Test
//    public void testReplaceToBlank() throws Exception {
//        log.info("testReplaceToBlank.");
//        String strToReplace = "ࢵ|�ǧ";
//        String text1 = "ࢵ�Ҵ�����";
//        String text2 = "�ǧ���Ե";
//
//        log.info("text1 output: {}",Util.replaceToBlank(text1,strToReplace));
//        log.info("text2 output: {}",Util.replaceToBlank(text2,strToReplace));
//
//        assertEquals("expected equal.", "�Ҵ�����", Util.replaceToBlank(text1,strToReplace));
//        assertEquals("expected equal.", "���Ե", Util.replaceToBlank(text2,strToReplace));
//        assertEquals("expected equal.", "", Util.replaceToBlank(null,strToReplace));
//        assertEquals("expected equal.", "ࢵ�Ҵ�����", Util.replaceToBlank(text1, null));
//        assertEquals("expected equal.", "", Util.replaceToBlank(null,null));
//    }

//    @Test
//    public void testIsTrue() throws Exception {
//        log.info("testIsTrue.");
//        assertTrue("expected true", Util.isTrue("1"));
//        assertTrue("expected true", Util.isTrue("1 "));
//        assertTrue("expected true", Util.isTrue("yes"));
//        assertTrue("expected true", Util.isTrue("Yes"));
//        assertTrue("expected true", Util.isTrue("true"));
//        assertTrue("expected true", Util.isTrue("True"));
//
//        assertTrue("expected true", Util.isTrue(1));
//
//        assertFalse("expected false", Util.isTrue("0"));
//    }

    @Test
    public void testIsNull() {
//        log.info("-- testIsNullObject.");
        try {
            assertTrue(Util.isNull(null));
            Date date = null;
            assertTrue("expected true" , Util.isNull(date));
            assertTrue("expected true", Util.isNull("Null"));
            assertTrue("expected true", Util.isNull("nuLL"));
            assertTrue("expected true", Util.isNull("                  nuLL"));
            assertTrue("expected true", Util.isNull("nuLL                  "));
        } catch (Exception e) {
//            log.error("-- Exception while testIsNull is processing [{}]", e.getMessage());
        }


    }
}

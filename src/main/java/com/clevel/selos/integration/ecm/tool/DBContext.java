package com.clevel.selos.integration.ecm.tool;

import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext implements Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    private EncryptionService encryptionService;
    @Inject
    @Config(name = "system.encryption.enable")
    private String encryptionEnable;
    @Inject
    @ExceptionMessage
    private Message msg;
    @Inject
    public DBContext() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (InstantiationException e) {
            log.error("", e);
        } catch (IllegalAccessException e) {
            log.error("", e);
        } catch (ClassNotFoundException e) {
            log.error("", e);
        }
    }

    public Connection getConnection(String jdbcURL, String user, String password) throws ECMInterfaceException {
        Connection conn = null;
        log.debug("DB URL: {}, User: {}, Password: HIDDEN", jdbcURL, user);

        try {
//            if (Util.isTrue(encryptionEnable)) {
//                password = encryptionService.decrypt(Base64.decodeBase64(password));
//            }
            conn = DriverManager.getConnection(jdbcURL, user, password);
        } catch (Exception e) {
            log.error("Exception while connect to database!", e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_CONNECT_ERROR, msg.get(ExceptionMapping.ECM_CONNECT_ERROR));
        }
        return conn;
    }
}

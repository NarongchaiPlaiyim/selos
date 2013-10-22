package com.clevel.selos.integration.rlos.csi.tool;

import com.clevel.selos.integration.RLOS;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext implements Serializable {
    @Inject
    @RLOS
    Logger log;

    @Inject
    EncryptionService encryptionService;
    @Inject
    @Config(name = "system.encryption.enable")
    String encryptionEnable;

    @Inject
    public DBContext() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (InstantiationException e) {
            log.error("",e);
        } catch (IllegalAccessException e) {
            log.error("",e);
        } catch (ClassNotFoundException e) {
            log.error("",e);
        }
    }

    public Connection getConnection(String jdbcURL,String user,String password) {
        Connection conn = null;
        log.debug("DB URL: {}, User: {}, Password: HIDDEN",jdbcURL,user);

        try {
            if (Util.isTrue(encryptionEnable)) {
                password = encryptionService.decrypt(Base64.decodeBase64(password));
            }
            conn = DriverManager.getConnection(jdbcURL,user,password);
        } catch (SQLException e) {
            log.error("Exception while connect to database!",e);
        }
        return conn;
    }
}

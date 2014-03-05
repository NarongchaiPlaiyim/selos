package com.clevel.selos.integration.bpm.tool;

import com.clevel.selos.integration.COMS;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;


public class SQLDBConnection implements Serializable
{

    @Inject
    @SELOS
    Logger log;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    EncryptionService encryptionService;
    @Inject
    @Config(name = "system.encryption.enable")
    String encryptionEnable;

    Connection conn = null;

   @Inject
   public SQLDBConnection()
    {
        try
        {
            log.info("In SQLDBConnection");

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        }
        catch(InstantiationException e)
        {

        }
        catch(IllegalAccessException e)
        {

        }
        catch(ClassNotFoundException e)
        {

        }
        catch(Exception e)
        {

        }
    }

    public Connection getConnection(String jdbcURL, String user, String password) throws Exception
    {


        log.debug("DB URL: {}, User: {}, Password: HIDDEN", jdbcURL, user);

        try
        {

            conn = DriverManager.getConnection(jdbcURL, user, password);

            log.info("connection ::::::::::::::{}",conn.toString());
        }
        catch(Exception e)
        {
            log.error("Exception while connect to PE database ",e);
        }

        return conn;
    }
}

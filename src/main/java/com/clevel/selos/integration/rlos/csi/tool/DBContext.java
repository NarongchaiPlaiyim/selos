package com.clevel.selos.integration.rlos.csi.tool;

import com.clevel.selos.integration.RLOS;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBContext implements Serializable {
    @Inject
    @RLOS
    Logger log;

    @Inject
    public DBContext() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(String jdbcURL,String user,String password) {
        Connection conn = null;
        log.debug("DB URL: {}, User: {}, Password: {}",jdbcURL,user,password);

        try {
            conn = DriverManager.getConnection(jdbcURL,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

package com.clevel.selos.integration.ecm.module;

import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ecm.tool.DBContext;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

public class DBExecute implements Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    @Config(name = "interface.coms.oracle.conn")
    private String connECM;
    @Inject
    @Config(name = "interface.coms.oracle.username")
    private String ecmUser;
    @Inject
    @Config(name = "interface.coms.oracle.password")
    private String ecmPassword;
    @Inject
    @Config(name = "interface.coms.oracle.schema")
    private String schema;
    @Inject
    private DBContext dbContext;
    @Inject
    @ExceptionMessage
    private Message msg;
    private Connection connection = null;
    private transient ResultSet resultSet = null;
    @Inject
    public DBExecute() {

    }
}

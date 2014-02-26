package com.clevel.selos.integration.ecm;

import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ecm.module.DBExecute;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class ECMService implements Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    private DBExecute dbExecute;
    @Inject
    @ExceptionMessage
    Message msg;
    @Inject
    public ECMService() {

    }
}

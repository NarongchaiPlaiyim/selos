package com.clevel.selos.integration.ecm;

import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class ECMInterfaceImpl implements ECMInterface, Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    @ExceptionMessage
    private Message msg;
    @Inject
    private ECMService ecmService;
    @Inject
    public ECMInterfaceImpl() {

    }
}

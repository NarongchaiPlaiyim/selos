package com.clevel.selos.integration.corebanking;

import com.clevel.selos.integration.RM;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CAInterfaceImpl {
    @Inject
    @RM
    Logger log;

    @Inject
    CAService caService;

    @Inject
    public CAInterfaceImpl() {
        log.info("=== RMInterfaceImpl()");
    }

}

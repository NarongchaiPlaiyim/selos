package com.clevel.selos.system.audit;

import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SystemAudit {
    @Inject
    Logger log;

    @Inject
    public SystemAudit() {
    }

}

package com.clevel.selos.system;

import com.clevel.selos.integration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LogProducer {
    @Produces
    Logger createLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RM
    Logger createRMLogger() {
        return LoggerFactory.getLogger(Integration.System.RM.name());
    }

    @Produces
    @NCB
    Logger createNCBLogger() {
        return LoggerFactory.getLogger(Integration.System.NCB.name());
    }

    @Produces
    @NCBI
    Logger createNCBILogger() {
        return LoggerFactory.getLogger(Integration.System.NCBI.name());
    }

    @Produces
    @DWH
    Logger createDWHLogger() {
        return LoggerFactory.getLogger(Integration.System.DWH.name());
    }

    @Produces
    @Email
    Logger createEmailLogger() {
        return LoggerFactory.getLogger(Integration.System.EMAIL.name());
    }

    @Produces
    @SW_ROSC
    Logger createSWROSCLogger() {
        return LoggerFactory.getLogger(Integration.System.SW_ROSC.name());
    }

    @Produces
    @BRMS
    Logger createBRMSLogger() {
        return LoggerFactory.getLogger(Integration.System.BRMS.name());
    }
}

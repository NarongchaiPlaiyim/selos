package com.clevel.selos.system;

import com.clevel.selos.integration.Integration;
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
    @Integration(Integration.System.RM)
    Logger createRMLogger() {
        return LoggerFactory.getLogger(Integration.System.RM.name());
    }

    @Produces
    @Integration(Integration.System.NCB)
    Logger createNCBLogger() {
        return LoggerFactory.getLogger(Integration.System.NCB.name());
    }

    @Produces
    @Integration(Integration.System.NCBI)
    Logger createNCBILogger() {
        return LoggerFactory.getLogger(Integration.System.NCBI.name());
    }

    @Produces
    @Integration(Integration.System.DWH)
    Logger createDWHLogger() {
        return LoggerFactory.getLogger(Integration.System.DWH.name());
    }

    @Produces
    @Integration(Integration.System.EMAIL)
    Logger createEmailLogger() {
        return LoggerFactory.getLogger(Integration.System.EMAIL.name());
    }

    @Produces
    @Integration(Integration.System.SW_ROSC)
    Logger createSWROSCLogger() {
        return LoggerFactory.getLogger(Integration.System.SW_ROSC.name());
    }

    @Produces
    @Integration(Integration.System.BRMS)
    Logger createBRMSLogger() {
        return LoggerFactory.getLogger(Integration.System.BRMS.name());
    }
}

package com.clevel.selos.system;

import com.clevel.selos.integration.*;
import com.clevel.selos.ws.WS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LogProducer {
    private static final String LOGGER_NAME_RM = "RM";
    private static final String LOGGER_NAME_NCB = "NCB";
    private static final String LOGGER_NAME_RLOS = "RLOS";
    private static final String LOGGER_NAME_EMAIL = "EMAIL";
    private static final String LOGGER_NAME_SAFEWATCH = "SAFEWATCH";
    private static final String LOGGER_NAME_BRMS = "BRMS";
    private static final String LOGGER_NAME_BPM = "BPM";
    private static final String LOGGER_NAME_ECM = "ECM";
    private static final String LOGGER_NAME_WS = "WS";
    private static final String LOGGER_NAME_DWH = "DWH";
    private static final String LOGGER_NAME_COMS = "COMS";

    @Produces
    @SELOS
    Logger createLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RM
    Logger createRMLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_RM);
    }

    @Produces
    @NCB
    Logger createNCBLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_NCB);
    }

    @Produces
    @RLOS
    Logger createRLOSLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_RLOS);
    }

    @Produces
    @Email
    Logger createEmailLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_EMAIL);
    }

    @Produces
    @SafeWatch
    Logger createSWROSCLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_SAFEWATCH);
    }

    @Produces
    @BRMS
    Logger createBRMSLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_BRMS);
    }

    @Produces
    @BPM
    Logger createBPMLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_BPM);
    }

    @Produces
    @ECM
    Logger createECMLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_ECM);
    }

    @Produces
    @WS
    Logger createWSLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_WS);
    }

    @Produces
    @DWH
    Logger createDWHLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_DWH);
    }

    @Produces
    @COMS
    Logger createCOMSLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_COMS);
    }
}

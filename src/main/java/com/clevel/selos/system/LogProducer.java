package com.clevel.selos.system;

import com.clevel.selos.integration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LogProducer {
    private static final String LOGGER_NAME_RM = "RM";
    private static final String LOGGER_NAME_NCB = "NCB";
    private static final String LOGGER_NAME_NCBI = "NCBI";
    private static final String LOGGER_NAME_DWH = "DWH";
    private static final String LOGGER_NAME_EMAIL = "EMAIL";
    private static final String LOGGER_NAME_SW_ROSC = "SW_ROSC";
    private static final String LOGGER_NAME_BRMS = "SW_BRMS";

    @Produces
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
    @NCBI
    Logger createNCBILogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_NCBI);
    }

    @Produces
    @DWH
    Logger createDWHLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_DWH);
    }

    @Produces
    @Email
    Logger createEmailLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_EMAIL);
    }

    @Produces
    @SW_ROSC
    Logger createSWROSCLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_SW_ROSC);
    }

    @Produces
    @BRMS
    Logger createBRMSLogger() {
        return LoggerFactory.getLogger(LOGGER_NAME_BRMS);
    }
}

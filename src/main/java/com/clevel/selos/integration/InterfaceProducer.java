package com.clevel.selos.integration;

import com.clevel.selos.integration.test.RMTest;

import javax.enterprise.inject.Produces;

public class InterfaceProducer {
    @Produces
    public RMInterface getRMInterface(@RM RMInterface rmInterface) {
        return rmInterface;
    }
}

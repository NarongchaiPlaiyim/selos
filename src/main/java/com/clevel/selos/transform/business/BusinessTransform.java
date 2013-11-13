package com.clevel.selos.transform.business;

import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public abstract class BusinessTransform implements Serializable {

    @Inject
    @SELOS
    Logger log;
}

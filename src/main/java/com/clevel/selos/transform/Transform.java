package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public abstract class Transform implements Serializable {
    @Inject
    @SELOS
    Logger log;
}

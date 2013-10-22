package com.clevel.selos.transform;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public abstract class Transform implements Serializable {

    @Inject
    Logger log;
}

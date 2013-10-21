package com.clevel.selos.transform;

import com.clevel.selos.security.UserDetail;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import java.io.Serializable;

public abstract class Transform implements Serializable {

    @Inject
    Logger log;
}

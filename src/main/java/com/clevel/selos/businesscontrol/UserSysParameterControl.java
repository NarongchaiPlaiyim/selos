package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserSysParameterControl extends BusinessControl{
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public UserSysParameterControl(){}

    public Object getSysParameterValue(String key, String defaultValue){


        return null;
    }
}

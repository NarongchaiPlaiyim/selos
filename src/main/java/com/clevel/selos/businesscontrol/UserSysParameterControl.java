package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.UserSysParameterView;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Map;



@Stateless
public class UserSysParameterControl extends BusinessControl{
    @Inject
    @SELOS
    private Logger logger;

    private static Map<String, UserSysParameterView> userSysParameterViewMap;

    @Inject
    public UserSysParameterControl(){
    }


    public Object getSysParameterValue(String key, String defaultValue){


        return null;
    }

    private boolean loadUserSysParameter(){
        try{

        } catch (Exception ex){

        }
        return true;
    }
}

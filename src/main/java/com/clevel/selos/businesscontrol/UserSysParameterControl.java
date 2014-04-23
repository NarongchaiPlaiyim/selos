package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserSysParameterDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserSysParameter;
import com.clevel.selos.model.view.UserSysParameterView;
import com.clevel.selos.transform.UserSysParameterTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class UserSysParameterControl extends BusinessControl{
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private UserSysParameterDAO userSysParameterDAO;

    @Inject
    private UserSysParameterTransform userSysParameterTransform;

    private static Map<String, UserSysParameterView> userSysParameterViewMap = null;
    private final static Object _mutexLock = new Object();

    @Inject
    public UserSysParameterControl(){
    }

    public void initialUserSysParameter(){
        if(userSysParameterViewMap == null){
            logger.debug("loadUserSysParameter");
            loadUserSysParameter();
        }
    }

    /*public List<String> getSysParameterValue(String key){
        initialUserSysParameter();

        return null;
    }

    public Integer getSysParameterValue(String key){
        initialUserSysParameter();

        return null;
    }*/

    private boolean loadUserSysParameter(){
        logger.debug("-- begin loadUserSysParameter {}", userSysParameterViewMap);
        try{
            Map<String, UserSysParameterView> _tmpUserSysParameterMap = new HashMap<String, UserSysParameterView>();
            List<UserSysParameter> userSysParameterList = userSysParameterDAO.findActiveAll();
            for(UserSysParameter userSysParameter : userSysParameterList){
                UserSysParameterView userSysParameterView = userSysParameterTransform.transformToView(userSysParameter);
                _tmpUserSysParameterMap.put(userSysParameterView.getCode(), userSysParameterView);
            }

            if(_tmpUserSysParameterMap.size() > 0){
                synchronized (_mutexLock){
                    userSysParameterViewMap = _tmpUserSysParameterMap;
                }
            }
        } catch (Exception ex){
            logger.debug("Cannot Load User Sys Parameter. Please Check the system");
        }
        logger.debug("-- end loadUserSysParameter {}", userSysParameterViewMap);
        return true;
    }
}

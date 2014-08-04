package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserSysParameterDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserSysParameter;
import com.clevel.selos.model.view.UserSysParameterView;
import com.clevel.selos.transform.UserSysParameterTransform;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
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

    public UserSysParameterView getSysParameterValue(String key){
        if(userSysParameterViewMap == null)
            loadData();
        return userSysParameterViewMap.get(key);
    }

    /*public List<String> getSysParameterValue(String key){
        initialUserSysParameter();

        return null;
    }

    public Integer getSysParameterValue(String key){
        initialUserSysParameter();

        return null;
    }*/

    public void loadData(){
        logger.debug("loadUserSysParameter");
        if(userSysParameterViewMap == null){
            synchronized (_mutexLock){
                if(userSysParameterViewMap == null)
                    userSysParameterViewMap = new HashMap<String, UserSysParameterView>();
            }
        }
        try{
            Map<String, UserSysParameterView> _tmpUserSysParameterMap = new HashMap<String, UserSysParameterView>();
            List<UserSysParameter> userSysParameterList = userSysParameterDAO.findActiveAll();
            for(UserSysParameter userSysParameter : userSysParameterList){
                UserSysParameterView userSysParameterView = userSysParameterTransform.transformToView(userSysParameter);
                _tmpUserSysParameterMap.put(userSysParameterView.getCode(), userSysParameterView);
            }

            if(_tmpUserSysParameterMap.size() > 0){
                synchronized (_mutexLock){
                    userSysParameterViewMap.putAll(_tmpUserSysParameterMap);
                }
            }
        } catch (Exception ex){
            logger.error("Cannot Load User Sys Parameter. Please Check the system", ex);
        }

    }
}

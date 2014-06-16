package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserAccessDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.UserAccess;
import com.clevel.selos.model.view.UserAccessView;
import com.clevel.selos.transform.UserAccessTransform;

import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserAccessControl extends BusinessControl {
    private static final long serialVersionUID = 8893510842761267033L;

	@Inject
    @SELOS
    private Logger log;

    @Inject
    private UserAccessDAO userAccessDAO;

    @Inject
    private UserAccessTransform userAccessTransform;

    @Inject
    public UserAccessControl(){

    }

    public List<UserAccessView> getUserAccessList(long stepId, int screenId){
        List<UserAccessView> userAccessViewList = new ArrayList<UserAccessView>();
        int roleId = getCurrentUser().getRole().getId();
        log.debug("getUserAccessList ::: stepId : {}, screenId : {}, roleId : {}", stepId, screenId, roleId);
        List<UserAccess> userAccessList = userAccessDAO.getUserAccess(stepId, screenId, roleId);

        if(userAccessList != null && userAccessList.size() > 0){
            userAccessViewList =  userAccessTransform.transformToViewList(userAccessList);
        }

        return userAccessViewList;
    }
    
    public boolean canUserAccess(Screen screen,long stepId) {
    	if (getCurrentUser() == null || screen == null || stepId <= 0)
    		return false;
    	int roleId = getCurrentUser().getRole().getId();
    	List<UserAccess> list = userAccessDAO.getUserAccess(stepId, screen.value(), roleId);
    	if (list == null || list.isEmpty())
    		return false;
    	for (UserAccess access : list) {
    		if (access.getAccessFlag() == 1)
    			return true;
    	}
    	return false;
    }
}

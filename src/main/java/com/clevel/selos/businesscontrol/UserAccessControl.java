package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserAccessDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserAccess;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserAccessControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private UserAccessDAO userAccessDAO;

    @Inject
    public UserAccessControl(){

    }

    public List<UserAccess> getUserAccessList(long stepId, int screenId){
        List<UserAccess> userAccessList = new ArrayList<UserAccess>();
        int roleId = getCurrentUser().getRole().getId();
        userAccessList = userAccessDAO.getUserAccess(stepId, screenId, roleId);

        return userAccessList;
    }
}

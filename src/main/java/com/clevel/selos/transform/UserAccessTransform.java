package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserAccess;
import com.clevel.selos.model.view.UserAccessView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UserAccessTransform extends Transform {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public UserAccessTransform(){

    }

    public UserAccessView transformToView(UserAccess userAccess){
        UserAccessView userAccessView = new UserAccessView();

        userAccessView.setScreenId(userAccess.getScreenId());
        userAccessView.setStepId(userAccess.getStep() != null ? userAccess.getStep().getId() : 0);
        userAccessView.setStageId(userAccess.getStage() != null ? userAccess.getStage().getId() : 0);
        userAccessView.setRoleId(userAccess.getRole() != null ? userAccess.getRole().getId() : 0);
        userAccessView.setAccessFlag(Util.isTrue(userAccess.getAccessFlag()));

        return userAccessView;
    }

    public List<UserAccessView> transformToViewList(List<UserAccess> userAccessList){
        List<UserAccessView> userAccessViewList = new ArrayList<UserAccessView>();

        for(UserAccess userAccess : userAccessList){
            UserAccessView userAccessView = transformToView(userAccess);
            userAccessViewList.add(userAccessView);
        }

        return userAccessViewList;
    }
}


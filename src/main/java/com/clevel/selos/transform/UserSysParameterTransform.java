package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserSysParameter;
import com.clevel.selos.model.view.UserSysParameterView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserSysParameterTransform extends Transform{
    @Inject
    @SELOS
    Logger logger;

    @Inject
    public UserSysParameterTransform(){}

    public UserSysParameterView transformToView(UserSysParameter userSysParameter){
        logger.debug("-- begin transformToView UserSysParameter {}", userSysParameter);
        if(userSysParameter == null)
            return null;

        UserSysParameterView userSysParameterView = new UserSysParameterView();
        userSysParameterView.setId(userSysParameter.getId());
        userSysParameterView.setCode(userSysParameter.getCode());
        userSysParameterView.setDescription(userSysParameter.getDescription());
        userSysParameterView.setActive(userSysParameter.isActive());
        userSysParameterView.setFieldType(userSysParameter.getFieldType());
        userSysParameterView.setName(userSysParameter.getName());
        userSysParameterView.setNoBDMSubmit(userSysParameter.isNoBDMSubmit());
        userSysParameterView.setPassBUApproval(userSysParameter.isPassBUApproval());
        userSysParameterView.setPassFinalApprovalStep(userSysParameter.isPassBUApproval());
        userSysParameterView.setValue(userSysParameter.getValue());
        logger.debug("-- end transformToView return {} ", userSysParameterView);
        return userSysParameterView;
    }
}

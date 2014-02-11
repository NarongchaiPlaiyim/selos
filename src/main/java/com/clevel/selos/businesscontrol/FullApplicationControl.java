package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FullApplicationControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;

    @Inject
    UserDAO userDAO;
    @Inject
    RoleDAO roleDAO;

    @Inject
    BPMExecutor bpmExecutor;

    public List<User> getABDMUserList(){
        User currentUser = getCurrentUser();
        Role abdmRole = roleDAO.findById(RoleValue.ABDM.id());

        List<User> abdmUserList = userDAO.findABDMList(currentUser, abdmRole);
        if(abdmUserList == null){
            abdmUserList = new ArrayList<User>();
        }
        log.debug("getABDMUserList : abdmUserList size : {}", abdmUserList.size());

        return abdmUserList;
    }

    public List<User> getZMUserList(){
        User currentUser = getCurrentUser();
        Role zmRole = roleDAO.findById(RoleValue.ZM.id());

        List<User> zmUserList = userDAO.findUserListByRole(currentUser, zmRole);
        if(zmUserList == null){
            zmUserList = new ArrayList<User>();
        }

        return zmUserList;
    }

    public void assignToABDM(String abdmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.assignToABDM(workCaseId, queueName, abdmUserId, ActionCode.ASSIGN_TO_ABDM.getVal());
    }

    public void submitToZM(String zmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.submitZM(workCaseId, queueName, zmUserId, ActionCode.SUBMIT_TO_ZM.getVal());
    }

    public void requestAppraisal(String queueName, long workCasePreScreenId, long workCaseId) throws Exception {
        bpmExecutor.requestAppraisal(workCasePreScreenId, workCaseId, queueName, ActionCode.REQUEST_APPRAISAL.getVal());
    }
}

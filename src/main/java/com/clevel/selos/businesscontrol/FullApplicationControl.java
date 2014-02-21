package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.WorkCaseAppraisalDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseAppraisal;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
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
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;

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

        List<User> zmUserList = userDAO.findUserZoneList(currentUser);
        if(zmUserList == null){
            zmUserList = new ArrayList<User>();
        }

        return zmUserList;
    }

    public List<User> getRMUserList(){
        User currentUser = getCurrentUser();

        List<User> rmUserList = userDAO.findUserRegionList(currentUser);
        if(rmUserList == null){
            rmUserList = new ArrayList<User>();
        }

        return rmUserList;
    }

    public List<User> getHeadUserList(){
        User currentUser = getCurrentUser();

        List<User> ghUserList = userDAO.findUserHeadList(currentUser);
        if(ghUserList == null){
            ghUserList = new ArrayList<User>();
        }

        return ghUserList;
    }

    public void assignToABDM(String abdmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.assignToABDM(workCaseId, queueName, abdmUserId, ActionCode.ASSIGN_TO_ABDM.getVal());
    }

    public void submitToZM(String zmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.submitZM(workCaseId, queueName, zmUserId, ActionCode.SUBMIT_TO_ZM.getVal());
    }

    public void requestAppraisalBDM(long workCasePreScreenId, long workCaseId) throws Exception{
        //update request appraisal flag
        if(workCasePreScreenId != 0){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setRequestAppraisal(1);
            workCasePrescreenDAO.persist(workCasePrescreen);
        } else if(workCaseId != 0) {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setRequestAppraisal(1);
            workCaseDAO.persist(workCase);
        } else {
            log.error("exception while Request Appraisal (BDM), can not find workcase or workcaseprescreen.");
            throw new Exception("exception while Request Appraisal, can not find case.");
        }
    }

    public void requestAppraisal(long workCasePreScreenId, long workCaseId) throws Exception{
        try{
            WorkCaseAppraisal workCaseAppraisal = createWorkCaseAppraisal(workCasePreScreenId, workCaseId);
            try{
                createWorkItemAppraisal(workCaseAppraisal);
                log.debug("create workcase for appraisal complete.");
            }catch (Exception ex){
                workCaseAppraisalDAO.delete(workCaseAppraisal);
                log.error("exception while launch workflow for appraisal, remove workcase appraisal.");
                throw new Exception(Util.getMessageException(ex));
            }
        }catch (Exception ex){
            throw new Exception(ex);
        }

    }

    public void createWorkItemAppraisal(WorkCaseAppraisal workCaseAppraisal) throws Exception{
        log.debug("requestAppraisal ::: workCaseAppraisal : {}", workCaseAppraisal);
        bpmExecutor.requestAppraisal(workCaseAppraisal.getAppNumber(), "", workCaseAppraisal.getProductGroup().getName(), workCaseAppraisal.getRequestType().getId(), getCurrentUserID());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public WorkCaseAppraisal createWorkCaseAppraisal(long workCasePreScreenId, long workCaseId) throws Exception {
        //Find all data in WorkCase or WorkCasePreScreen
        WorkCasePrescreen workCasePrescreen;
        WorkCase workCase;
        String appNumber = "";
        ProductGroup productGroup = null;
        RequestType requestType = null;
        log.debug("requestAppraisal ::: start...");
        if(Long.toString(workCasePreScreenId) != null && workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            log.debug("requestAppraisal ::: getAppNumber from workCasePrescreen : {}", workCasePrescreen);
            if(workCasePrescreen != null){
                appNumber = workCasePrescreen.getAppNumber();
                productGroup = workCasePrescreen.getProductGroup();
                requestType = workCasePrescreen.getRequestType();
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from prescreen");
            }
        }else if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            log.debug("requestAppraisal ::: getAppNumber from workCase : {}", workCase);
            if(workCase != null){
                appNumber = workCase.getAppNumber();
                productGroup = workCase.getProductGroup();
                requestType = workCase.getRequestType();
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from full application");
            }
        }else{
            throw new Exception("exception while request appraisal, cause session variable expired.");
        }

        //TODO Insert data into WRK_APPRAISAL
        WorkCaseAppraisal workCaseAppraisal = new WorkCaseAppraisal();
        workCaseAppraisal.setAppNumber(appNumber);
        workCaseAppraisal.setCreateDate(DateTime.now().toDate());
        workCaseAppraisal.setCreateBy(getCurrentUser());
        workCaseAppraisal.setModifyDate(DateTime.now().toDate());
        workCaseAppraisal.setModifyBy(getCurrentUser());
        workCaseAppraisal.setRequestDate(DateTime.now().toDate());
        workCaseAppraisal.setRequestBy(getCurrentUser());
        workCaseAppraisal.setProductGroup(productGroup);
        workCaseAppraisal.setRequestType(requestType);
        workCaseAppraisalDAO.persist(workCaseAppraisal);

        return workCaseAppraisal;
    }
}

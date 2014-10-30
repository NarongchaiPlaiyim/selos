package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.FieldsControlDAO;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.FieldsControl;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.transform.FieldsControlTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class MandatoryFieldsControl extends BusinessControl {
    private static final long serialVersionUID = 910567548215853330L;

	@Inject
    @SELOS
    private Logger log;

    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    FieldsControlDAO fieldsControlDAO;

    @Inject
    FieldsControlTransform fieldsControlTransform;

    Status status;
    User user;
    long stepId;


    protected List<FieldsControlView> initialCreation(Screen screen) {
        log.debug("initialCreation - Screen : {}",screen);
        HttpSession session = FacesUtil.getSession(false);

        if (session.getAttribute("workCaseId") != null){
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            status = workCase.getStatus();
        }

        user = getCurrentUser();

        if(session.getAttribute("stepId") != null){
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }

        List<FieldsControl> fieldsControlList = fieldsControlDAO.findFieldControlByScreenRoleStepStatus(screen.value(), user.getRole(), status, stepId);
        List<FieldsControlView> fieldsControlViewList = fieldsControlTransform.transformToViewList(fieldsControlList);
        return fieldsControlViewList;
    }
    
    public List<FieldsControlView> getFieldsControlView(long workCaseId, Screen screen, String caseOwnerUserId) {
        String currentUserId = getCurrentUserID();
        if(caseOwnerUserId.toLowerCase().equalsIgnoreCase(currentUserId.toLowerCase())) {
            if (workCaseId <= 0 || screen == null)
                return Collections.emptyList();
            User user = getCurrentUser();
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            Status status = workCase.getStatus();
            HttpSession session = FacesUtil.getSession(false);
            if(session.getAttribute("stepId") != null){
                stepId = Long.parseLong(session.getAttribute("stepId").toString());
            }
            log.debug("get Field control for screen " + screen + " ,Workcase=" + workCaseId + " ,stepId=" + stepId + " ,role=" + user.getRole());
            List<FieldsControl> fieldsControlList = fieldsControlDAO.findFieldControlByScreenRoleStepStatus(screen.value(), user.getRole(), status, stepId);
            List<FieldsControlView> fieldsControlViewList = fieldsControlTransform.transformToViewList(fieldsControlList);
            log.debug("Result fields control = " + fieldsControlViewList.size());
            return fieldsControlViewList;
        }else{
            return Collections.emptyList();
        }
    }

    //FULLAPP
    public List<FieldsControlView> getFieldsControlView(long workCaseId, long stepId, long statusId, Screen screen, String caseOwnerUserId) {
        String currentUserId = getCurrentUserID();
        log.debug("get Field control for screen : {}, stepId : {}, statusId : {}, currentUserId : {}, caseOwnerUserId : {}", screen, stepId, statusId, currentUserId, caseOwnerUserId);
        if(caseOwnerUserId.toLowerCase().equalsIgnoreCase(currentUserId.toLowerCase())) {
            if (stepId <= 0 || workCaseId <= 0 || screen == null)
                return Collections.emptyList();
            User user = getCurrentUser();
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            int productGroupId = workCase.getProductGroup().getId();
            List<FieldsControl> fieldsControlList = fieldsControlDAO.findFieldControl(screen.value(), user.getRole(), stepId, statusId, productGroupId, 0);
            List<FieldsControlView> fieldsControlViewList = fieldsControlTransform.transformToViewList(fieldsControlList);
            log.debug("Result fields control : {} ", fieldsControlViewList.size());
            return fieldsControlViewList;
        }else{
            return Collections.emptyList();
        }
    }

    public List<FieldsControlView> getFieldsControlViewPreScreen(long workCasePreScreenId, long stepId, long statusId, Screen screen, String caseOwnerUserId) {
        String currentUserId = getCurrentUserID();
        if(caseOwnerUserId.toLowerCase().equalsIgnoreCase(currentUserId.toLowerCase())) {
            if (stepId <= 0 || workCasePreScreenId <= 0 || screen == null)
                return Collections.emptyList();
            User user = getCurrentUser();
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            int productGroupId = 1;
            if(!Util.isNull(workCasePrescreen.getProductGroup())){
                productGroupId = workCasePrescreen.getProductGroup().getId();
            }
            log.debug("get Field control for screen : {}, stepId : {}, statusId : {}, role : {}", screen, stepId, statusId, user.getRole());
            List<FieldsControl> fieldsControlList = fieldsControlDAO.findFieldControl(screen.value(), user.getRole(), stepId, statusId, productGroupId, 0);
            List<FieldsControlView> fieldsControlViewList = fieldsControlTransform.transformToViewList(fieldsControlList);
            log.debug("Result fields control : {} ", fieldsControlViewList.size());
            return fieldsControlViewList;
        }else{
            return Collections.emptyList();
        }
    }

    /*public List<FieldsControlView> getFieldsControlViewPreeScreen(long workCasePreScreenId, long stepId, Screen screen, String caseOwnerUserId) {
        String currentUserId = getCurrentUserID();
        if(caseOwnerUserId.toLowerCase().equalsIgnoreCase(currentUserId.toLowerCase())) {
            if (stepId <= 0 || workCasePreScreenId <= 0 || screen == null)
                return Collections.emptyList();
            User user = getCurrentUser();
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            if(workCasePrescreen!=null && workCasePrescreen.getId()>0){
                Prescreen prescreen = prescreenDAO.findByWorkCasePrescreen(workCasePrescreen);
                if(prescreen!=null && prescreen.getId()>0){
                    int productGroupId = prescreen.getProductGroup().getId();
                    log.debug("get Field control for screen : {}, stepId : {}, role : {}", screen, stepId, user.getRole());
                    List<FieldsControl> fieldsControlList = fieldsControlDAO.findFieldControl(screen.value(), user.getRole(), stepId, productGroupId, 0);
                    List<FieldsControlView> fieldsControlViewList = fieldsControlTransform.transformToViewList(fieldsControlList);
                    log.debug("Result fields control : {} ", fieldsControlViewList.size());
                    return fieldsControlViewList;
                }
            }
            return Collections.emptyList();
        }else{
            return Collections.emptyList();
        }
    }*/

    /*public List<FieldsControlView> getFieldsControlView(long workCaseId, Screen screen, int productProgramId, int specialTypeId, String caseOwnerUserId) {
        String currentUserId = getCurrentUserID();
        log.debug("getFieldsControlView : caseOwnerUserId : {}, currentUserId : {}", caseOwnerUserId, currentUserId);
        if(caseOwnerUserId.toLowerCase().equalsIgnoreCase(currentUserId.toLowerCase())) {
            if (workCaseId <= 0 || screen == null)
                return Collections.emptyList();
            User user = getCurrentUser();
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            long stepId = workCase.getStep().getId();
            int productGroupId = workCase.getProductGroup().getId();
            List<FieldsControl> fieldsControlList = fieldsControlDAO.findFieldControl(screen.value(), user.getRole(), stepId, productGroupId, specialTypeId);
            List<FieldsControlView> fieldsControlViewList = fieldsControlTransform.transformToViewList(fieldsControlList);
            return fieldsControlViewList;
        }else{
            return Collections.emptyList();
        }
    }*/
}

package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.master.FieldsControlDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerCSI;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.FieldsControlTransform;
import com.clevel.selos.transform.business.CustomerBizTransform;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MandatoryFieldsControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    FieldsControlDAO fieldsControlDAO;

    @Inject
    FieldsControlTransform fieldsControlTransform;

    WorkCasePrescreen workCasePrescreen;
    WorkCase workCase;
    Status status;
    User user;
    long stepId;

    protected List<FieldsControlView> initialCreation(Screen screen) {
        log.debug("initialCreation - Screen : {}",screen);
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCasePreScreenId") != null){
            long workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            status = workCasePrescreen.getStatus();
        } else if (session.getAttribute("workCaseId") != null){
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            workCase = workCaseDAO.findById(workCaseId);
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
}

package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.InboxView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InboxBizTransform extends BusinessTransform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    StepDAO stepDAO;

    public List<InboxView> transformToView(List<CaseDTO> caseDTOs) {
        List<InboxView> inboxViewList = new ArrayList<InboxView>();
        int listKey = 0;
        for (CaseDTO item : caseDTOs) {
            InboxView inboxView = new InboxView();
            inboxView.setListKey(listKey);
            if (item.getCaseData().containsKey("F_WobNum")) {
                inboxView.setFnWobNum(item.getCaseData().get("F_WobNum"));
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(inboxView.getFnWobNum());
                if (workCasePrescreen != null) {
                    inboxView.setWorkCasePreScreenId(workCasePrescreen.getId());
                    inboxView.setCaNo(workCasePrescreen.getCaNumber());
                    inboxView.setBdmId(((User) workCasePrescreen.getCreateBy()).getId());
                    inboxView.setStatusCode(workCasePrescreen.getStatus().getId());
                    inboxView.setRequestAppraisal(workCasePrescreen.getRequestAppraisal());
                    inboxView.setStatusCode(workCasePrescreen.getStatus().getId());
                } else {
                    inboxView.setWorkCasePreScreenId(0);
                    inboxView.setCaNo("");
                    inboxView.setBdmId("");
                }
                WorkCase workCase = workCaseDAO.findByWobNumber(inboxView.getFnWobNum());
                if (workCase != null) {
                    inboxView.setWorkCaseId(workCase.getId());
                    //inboxView.setCaNo(workCase.getCaNumber());
                    inboxView.setBdmId(workCase.getCreateBy().getId());
                    inboxView.setRequestAppraisal(workCase.getRequestAppraisal());
                    inboxView.setStatusCode(workCase.getStatus().getId());
                }

            }
            if (item.getCaseData().containsKey("F_StepName")) {
                inboxView.setFnStepName(item.getCaseData().get("F_StepName"));
            }
            if (item.getCaseData().containsKey("Step_Code")) {
                inboxView.setStepCode(item.getCaseData().get("Step_Code"));
                Step step = stepDAO.findById(Long.parseLong(inboxView.getStepCode().toString()));
                inboxView.setStepId(step.getId());
            }

            if (item.getCaseData().containsKey("Lock_Status")) {
                inboxView.setLockStatus(item.getCaseData().get("Lock_Status"));
            }

            if (item.getCaseData().containsKey("Locked_User")) {
                inboxView.setLockedUser(item.getCaseData().get("Locked_User"));
            }

            if (item.getCaseData().containsKey("QUEUE_NAME")) {
                inboxView.setQueueName(item.getCaseData().get("QUEUE_NAME"));
            }
            listKey = listKey + 1;
            inboxViewList.add(inboxView);
        }
        return inboxViewList;
    }
}

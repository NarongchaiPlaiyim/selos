package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.view.InboxView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InboxBizTransform extends BusinessTransform {
    @Inject
    Logger log;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    StepDAO stepDAO;

    public List<InboxView> transformToView(List<CaseDTO> caseDTOs){
        List<InboxView> inboxViewList = new ArrayList<InboxView>();
        for(CaseDTO item : caseDTOs){
            InboxView inboxView = new InboxView();

            if(item.getCaseData().containsKey("F_WobNum")){
                inboxView.setFnWobNum(item.getCaseData().get("F_WobNum"));
                inboxView.setWorkCasePreScreenId(workCasePrescreenDAO.findIdByWobNumber(inboxView.getFnWobNum()));
                inboxView.setWorkCaseId(workCaseDAO.findIdByWobNumber(inboxView.getFnWobNum()));
            }
            if(item.getCaseData().containsKey("F_StepName")){
                inboxView.setFnStepName(item.getCaseData().get("F_StepName"));
            }
            if(item.getCaseData().containsKey("Step_Code")){
                inboxView.setStepCode(item.getCaseData().get("Step_Code"));
                Step step = stepDAO.findById(Long.parseLong(inboxView.getStepCode().toString()));
                inboxView.setStepId(step.getId());
            }

            if(item.getCaseData().containsKey("Lock_Status")){
                inboxView.setLockStatus(item.getCaseData().get("Lock_Status"));
            }

            if(item.getCaseData().containsKey("Locked_User")){
                inboxView.setLockedUser(item.getCaseData().get("Locked_User"));
            }

            if(item.getCaseData().containsKey("QUEUE_NAME")){
                inboxView.setQueueName(item.getCaseData().get("QUEUE_NAME"));
            }

            inboxViewList.add(inboxView);
        }
        return inboxViewList;
    }
}

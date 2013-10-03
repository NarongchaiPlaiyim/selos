package com.clevel.selos.transform.business;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.model.view.InboxView;

import java.util.ArrayList;
import java.util.List;

public class InboxBizTransform extends BusinessTransform {

    public List<InboxView> transformToView(List<CaseDTO> caseDTOs){
        List<InboxView> inboxViewList = new ArrayList<InboxView>();
        for(CaseDTO item : caseDTOs){
            InboxView inboxView = new InboxView();

            if(item.getCaseData().containsKey("F_WobNum")){
                inboxView.setFnWobNum(item.getCaseData().get("F_WobNum"));
            }
            if(item.getCaseData().containsKey("F_StepName")){
                inboxView.setFnStepName(item.getCaseData().get("F_StepName"));
            }
            if(item.getCaseData().containsKey("Step_Code")){
                inboxView.setStepCode(item.getCaseData().get("Step_Code"));
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

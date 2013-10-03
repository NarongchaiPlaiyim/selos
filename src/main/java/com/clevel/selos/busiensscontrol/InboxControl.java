package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.transform.business.InboxBizTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class InboxControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    BPMInterface bpmInterface;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    InboxBizTransform inboxBizTransform;

    public List<InboxView> getInboxFromBPM(UserDetail userDetail){
        List<InboxView> inboxViewList = new ArrayList<InboxView>();

        List<CaseDTO> caseDTOList = bpmInterface.getInboxList();

        /*List<CaseDTO> caseDTOList = new ArrayList<CaseDTO>();


        List<WorkCasePrescreen> workCasePrescreenList = getWorkCasePreScreen();

        for(WorkCasePrescreen workCasePrescreen : workCasePrescreenList){
            CaseDTO caseDTO = new CaseDTO();
            HashMap<String, String> caseData = new HashMap<String, String>();
            caseData.put("F_WobNum", workCasePrescreen.getWobNumber());
            caseData.put("F_StepName", "PS1001");
            caseData.put("Step_Code", "1001");
            caseData.put("Lock_Status", "0");
            caseData.put("Locked_User", "0");
            caseData.put("QUEUE_NAME", "0");

            caseDTO.setCaseData(caseData);

            caseDTOList.add(caseDTO);
        }
*/
        log.info("CaseDTO : caseDTOList : {}", caseDTOList);
        inboxViewList = inboxBizTransform.transformToView(caseDTOList);

        return inboxViewList;
    }

    //Tempory to remove
    public List<WorkCasePrescreen> getWorkCasePreScreen(){
        List<WorkCasePrescreen> workCasePrescreenList = workCasePrescreenDAO.findAll();

        return workCasePrescreenList;
    }
}

package com.clevel.selos.busiensscontrol;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.transform.business.InboxBizTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class InboxControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    BPMInterface bpmInterface;

    @Inject
    InboxBizTransform inboxBizTransform;

    public List<InboxView> getInboxFromBPM(UserDetail userDetail){
        List<InboxView> inboxViewList = new ArrayList<InboxView>();

        List<CaseDTO> caseDTOList = bpmInterface.getInboxList();

        inboxViewList = inboxBizTransform.transformToView(caseDTOList);

        return inboxViewList;
    }
}

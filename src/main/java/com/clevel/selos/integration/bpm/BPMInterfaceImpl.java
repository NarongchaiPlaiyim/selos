package com.clevel.selos.integration.bpm;

import com.clevel.selos.filenet.bpm.connection.dto.UserDTO;
import com.clevel.selos.filenet.bpm.connection.helper.SELOSConnectionHelper;
import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.filenet.bpm.services.impl.BPMServiceImpl;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.IntegrationStatus;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.system.Config;
import com.clevel.selos.ws.WSDataPersist;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Default
public class BPMInterfaceImpl implements BPMInterface {
    @Inject
    @BPM
    Logger log;

    @Inject
    @Config(name = "interface.bpm.ce.uri")
    String ceURI;
    @Inject
    @Config(name = "interface.bpm.jaas.name")
    String jaasName;
    @Inject
    @Config(name = "interface.bpm.objectStore")
    String objectStore;
    @Inject
    @Config(name = "interface.bpm.pe.connectionPoint")
    String connectionPoint;
    @Inject
    @Config(name = "interface.bpm.client.date.format")
    String clientDateFormat;
    @Inject
    @Config(name = "interface.bpm.system.field.inbox")
    String inboxFields;
    @Inject
    @Config(name = "interface.bpm.workflow.name")
    String workflowName;
    @Inject
    @Config(name = "interface.bpm.username")
    String bpmUsername;
    @Inject
    @Config(name = "interface.bpm.password")
    String bpmPassword;

    @Inject
    WSDataPersist wsDataPersist;

    @Inject
    public BPMInterfaceImpl() {
    }

    @Override
    public boolean createCase(CaseCreationHistory caseCreationHistory){
        log.debug("createCase. (detail: {})",caseCreationHistory);
        boolean success = true;

        wsDataPersist.addNewCase(caseCreationHistory);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("");
        userDTO.setPassword("");
        HashMap<String,String> caseParameter = new HashMap<String, String>();
        caseParameter.put("BDMUserName", caseCreationHistory.getBdmId());
        caseParameter.put("CANumber", caseCreationHistory.getCaNumber());

        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(userDTO);
            bpmService.launchCase(caseParameter);
        } catch (SELOSBPMException e) {
            success = false;
            log.error("Exception while create case in BPM!",e);
        }
        return success;
    }
}

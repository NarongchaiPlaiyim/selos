package com.clevel.selos.integration.bpm;

import com.clevel.selos.filenet.bpm.connection.dto.UserDTO;
import com.clevel.selos.filenet.bpm.connection.helper.SELOSConnectionHelper;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.filenet.bpm.services.impl.BPMServiceImpl;
import com.clevel.selos.filenet.bpm.util.constants.BPMConstants;
import com.clevel.selos.filenet.bpm.util.resources.BPMConfigurationsDTO;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.IntegrationStatus;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.system.Config;
import com.clevel.selos.ws.WSDataPersist;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import filenet.vw.api.VWSession;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.security.auth.Subject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

        log.debug("ceuri: {}, username: {}, password: {}", ceURI, bpmUsername, bpmPassword);

        HashMap<String,String> caseParameter = new HashMap<String, String>();
        caseParameter.put("JobName",caseCreationHistory.getJobName());
        caseParameter.put("CANumber", caseCreationHistory.getCaNumber());
        caseParameter.put("RefCANumber", caseCreationHistory.getOldCaNumber());
        caseParameter.put("AccountNo1", caseCreationHistory.getAccountNo1());
        caseParameter.put("CustomerId", caseCreationHistory.getCustomerId());
        caseParameter.put("CustomerName", caseCreationHistory.getCustomerName());
        caseParameter.put("CitizenId", caseCreationHistory.getCitizenId());
        caseParameter.put("RequestType", Integer.toString(caseCreationHistory.getRequestType()));
        caseParameter.put("CustomerType", Integer.toString(caseCreationHistory.getCustomerType()));
        caseParameter.put("BDMUserName", caseCreationHistory.getBdmId());
        caseParameter.put("HubCode", caseCreationHistory.getHubCode());
        caseParameter.put("RegionCode", caseCreationHistory.getRegionCode());
        caseParameter.put("AppInDateBDM", caseCreationHistory.getAppInDateBDM());
        caseParameter.put("AppNumber", caseCreationHistory.getAppRefNumber());
        caseParameter.put("RefAppNumber", "");
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getUserDTO(),getConfigurationDTO());
            bpmService.launchCase(caseParameter);
        } catch (SELOSBPMException e) {
            success = false;
            caseCreationHistory.setStatus(IntegrationStatus.FAILED);
            caseCreationHistory.setStatusDetail(e.getMessage());
            log.error("Exception while create case in BPM!",e);
        }

        wsDataPersist.addNewCase(caseCreationHistory);
        return success;
    }

    public List<CaseDTO> getInboxList() {
        log.debug("getInboxList.");
        List<CaseDTO> caseDTOs = Collections.emptyList();
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getUserDTO(),getConfigurationDTO());
            caseDTOs = bpmService.getCases(BPMConstants.BPM_QUEUE_PERSONAL_INBOX_NAME,BPMConstants.BPM_QUEUE_TYPE_PERSONALQ,null,null);
        } catch (SELOSBPMException e) {
            log.error("Exception while create case in BPM!",e);
        }

        log.debug("getInboxList. (result size: {})",caseDTOs.size());
        return caseDTOs;
    }

    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(bpmUsername);
        userDTO.setPassword(bpmPassword);
        return userDTO;
    }

    private BPMConfigurationsDTO getConfigurationDTO() {
        BPMConfigurationsDTO bpmConfigurationsDTO = new BPMConfigurationsDTO();
        bpmConfigurationsDTO.setCEURI(ceURI);
        bpmConfigurationsDTO.setCaseWorkflowName(workflowName);
        bpmConfigurationsDTO.setConnectionPointName(connectionPoint);
        bpmConfigurationsDTO.setJassName(jaasName);
        bpmConfigurationsDTO.setObjectStoreName(objectStore);
        return bpmConfigurationsDTO;
    }
}

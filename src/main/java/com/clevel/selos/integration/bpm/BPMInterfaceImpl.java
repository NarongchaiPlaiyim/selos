package com.clevel.selos.integration.bpm;

import com.clevel.selos.filenet.bpm.connection.dto.UserDTO;
import com.clevel.selos.filenet.bpm.connection.helper.SELOSConnectionHelper;
import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.filenet.bpm.services.impl.BPMServiceImpl;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.system.Config;
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
    public BPMInterfaceImpl() {
    }

    @Override
    public void createCase(String bdmUsername, String caNumber) throws SELOSBPMException {
        log.debug("createCase.");
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("");
        userDTO.setPassword("");
        BPMServiceImpl bpmService = new BPMServiceImpl(userDTO);
        HashMap<String,String> caseParameter = new HashMap<String, String>();
        caseParameter.put("BDMUserName",bdmUsername);
        caseParameter.put("CANumber",caNumber);
        bpmService.launchCase(caseParameter);
    }
}

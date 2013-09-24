package com.clevel.selos.integration.bpm;

import com.clevel.selos.filenet.bpm.connection.dto.UserDTO;
import com.clevel.selos.filenet.bpm.connection.helper.SELOSConnectionHelper;
import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.filenet.bpm.services.impl.BPMServiceImpl;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.BPMInterface;
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
    public BPMInterfaceImpl() {
    }

    public void createCase() throws SELOSBPMException {
        log.debug("createCase.");
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("");
        userDTO.setPassword("");
        BPMServiceImpl bpmService = new BPMServiceImpl(userDTO);
        Map<String,String> caseParameter = new HashMap<String, String>();
//        bpmService.launchWorkflow(caseParameter,"processName");
    }
}

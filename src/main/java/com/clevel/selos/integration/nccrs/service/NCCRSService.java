package com.clevel.selos.integration.nccrs.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.test.NCCRSInterfaceImpTest;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class NCCRSService implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    NCCRSImp nccrsImp;

    @Inject
    @ValidationMessage
    Message message;

    @Inject
    NCCRSInterfaceImpTest nccrsInterfaceImpTest;
    public final String ERROR = "ER01001";

    @Inject
    public NCCRSService() {
    }

    public void process(NCCRSModel nccrsModel){
        try {
            log.debug("=========================================NCCRS process.");
            NCCRSResponseModel responseModel = nccrsInterfaceImpTest.request(nccrsModel);

            log.debug("=========================================NCCRS User. {}",responseModel.getHeader().getUser());

        } catch (Exception e) {
            log.error("=========================================NCCRS Exception : {}", e);
        }

    }
}

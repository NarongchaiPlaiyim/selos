package com.clevel.selos.integration.ncb;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.nccrs.service.NCCRSService;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.service.NCRSService;
import com.clevel.selos.integration.ncb.vaildation.ValidationImp;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCBInterfaceImpl implements NCBInterface, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    NCCRSService nccrsService;

    @Inject
    NCRSService ncrsService;

    @Inject
    ValidationImp validationImp;

    @Inject
    public NCBInterfaceImpl() {
    }

    @Override
    public ArrayList<NCRSOutputModel> request(NCRSInputModel inputModel) throws Exception {
        log.info("NCRS request");
//        validationImp.validation(inputModel);
        return ncrsService.process(inputModel);
    }

    @Override
    public ArrayList<NCCRSOutputModel> request(NCCRSInputModel inputModel) throws Exception {
        log.info("NCCRS request");
//        validationImp.validation(inputModel);
        return nccrsService.process(inputModel);
    }
}

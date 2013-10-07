package com.clevel.selos.integration.ncb;

import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.ncb.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.nccrs.service.NCCRSService;
import com.clevel.selos.integration.ncb.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.service.NCRSService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

public class NCBInterfaceImpl implements NCBInterface, Serializable {
    @Inject
    NCCRSService nccrsService;

    @Inject
    NCRSService ncrsService;

    @Inject
    public NCBInterfaceImpl() {
    }

    @Override
    public ArrayList<NCRSOutputModel> request(NCRSInputModel inputModel) throws Exception {
        ///Validation yung mai dai tum na ja
        return ncrsService.process(inputModel);
    }

    @Override
    public ArrayList<NCCRSOutputModel> request(NCCRSInputModel inputModel) throws Exception {
        ///Validation
        return nccrsService.process(inputModel);
    }
}

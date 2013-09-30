package com.clevel.selos.integration;

import com.clevel.selos.integration.ncb.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;

public interface NCBInterface {
    public NCRSResponseModel request(NCRSModel ncrsModel)throws Exception;
    public NCCRSResponseModel request(NCCRSInputModel nccrsModel)throws Exception;
}

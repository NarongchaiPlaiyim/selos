package com.clevel.selos.integration;

import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.nccrs.service.NCCRSModel;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSModel;

public interface NCBInterface {
    public NCRSResponseModel request(NCRSModel ncrsModel)throws Exception;
    public NCCRSResponseModel request(NCCRSModel nccrsModel)throws Exception;
}

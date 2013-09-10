package com.clevel.selos.integration;

import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.nccrs.service.NCCRSModel;

public interface NCCRSInterface {
    public NCCRSResponseModel request(NCCRSModel nccrsModel)throws Exception;
}

package com.clevel.selos.integration.nccrs.service;

import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;

public interface NCCRS {
    public NCCRSResponseModel requestOnline(NCCRSModel model)throws Exception;
    public NCCRSResponseModel requestOffline(NCCRSModel model)throws Exception;
}

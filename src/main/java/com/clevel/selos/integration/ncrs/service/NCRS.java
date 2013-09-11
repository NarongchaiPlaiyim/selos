package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;

public interface NCRS {
    public NCRSResponseModel requestOnline(NCRSModel ncrsModel)throws Exception;
    public NCRSResponseModel requestOffline(NCRSModel ncrsModel)throws Exception;
}

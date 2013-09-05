package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.response.NcrsResponse;

public interface NCRS {
    public NcrsResponse requestOnline(NCRSModel ncrsModel)throws Exception;
    public NcrsResponse requestOffline(NCRSModel ncrsModel)throws Exception;
}

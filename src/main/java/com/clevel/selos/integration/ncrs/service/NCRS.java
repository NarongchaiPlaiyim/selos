package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.response.NCRSResponse;

public interface NCRS {
    public NCRSResponse requestOnline(NCRSModel ncrsModel)throws Exception;
    public NCRSResponse requestOffline(NCRSModel ncrsModel)throws Exception;
}

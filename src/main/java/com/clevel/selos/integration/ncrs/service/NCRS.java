package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.response.Ncrsresponse;

public interface NCRS {
    public Ncrsresponse requestOnline(NCRSModel ncrsModel)throws Exception;
    public Ncrsresponse requestOffline(NCRSModel ncrsModel)throws Exception;
}

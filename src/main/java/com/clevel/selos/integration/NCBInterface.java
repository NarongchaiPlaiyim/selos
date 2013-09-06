package com.clevel.selos.integration;


import com.clevel.selos.integration.ncrs.models.response.NCRSResponse;
import com.clevel.selos.integration.ncrs.service.NCRSModel;

public interface NCBInterface {
    public NCRSResponse request(NCRSModel ncrsModel)throws Exception;
}

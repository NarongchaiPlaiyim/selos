package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.integration.Integration;
import com.clevel.selos.integration.ncrs.models.response.Ncrsresponse;
import org.slf4j.Logger;

import javax.inject.Inject;

public class NCRSService {
    @Inject
    @Integration(Integration.System.NCB)
    Logger log;
    public NCRSService() {
        process();
    }
    public void process(){
        log.debug("process().");
        NCRSModel ncrsModel = new NCRSModel();
        NCRS ncrs = new NCRSImp();

        try {
            Ncrsresponse ncrsresponse =  ncrs.requestOnline(ncrsModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

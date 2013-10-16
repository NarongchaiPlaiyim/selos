package com.clevel.selos.integration.dwh;

import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.model.Obligation;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Default
public class DWHInterfaceImpl implements DWHInterface,Serializable{
    @Inject
    @DWH
    Logger log;

    @Inject
    DWHService dwhService;

    @Inject
    public DWHInterfaceImpl() {

    }


    @Override
    public List<Obligation> getObligation(String userId, List<String> tmbCusIdList) {
        log.debug("getObligation (userId : {}, tmbCusIdList : {})",userId,tmbCusIdList);
        List<Obligation> obligationList = Collections.EMPTY_LIST;
        if(tmbCusIdList!=null && tmbCusIdList.size()>0){
            obligationList = dwhService.getObligationByTmbCusId(tmbCusIdList);
            log.debug("getObligation result (obligationList size : {})",obligationList.size());
        }
        return obligationList;
    }
}

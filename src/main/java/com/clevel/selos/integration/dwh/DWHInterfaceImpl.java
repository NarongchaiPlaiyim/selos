package com.clevel.selos.integration.dwh;

import com.clevel.selos.integration.DWH;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.model.Obligation;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Default
public class DWHInterfaceImpl implements DWHInterface,Serializable{
    @Inject
    @DWH
    Logger log;

    @Inject
    public DWHInterfaceImpl() {

    }


    @Override
    public List<Obligation> getObligation(String userId,String tmbCusId) {
        return null;
    }
}

package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateDocAccess;
import com.clevel.selos.model.view.master.MandateDocAccessView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MandateDocAccessTransform extends Transform {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public MandateDocAccessTransform(){}

    public MandateDocAccessView transformToView(MandateDocAccess mandateDocAccess){
        MandateDocAccessView mandateDocAccessView = new MandateDocAccessView();
        if(mandateDocAccess == null)
            return mandateDocAccessView;

        mandateDocAccessView.setId(mandateDocAccess.getId());
        mandateDocAccessView.setAccessType(mandateDocAccess.getAccessType());
        mandateDocAccessView.setActive(mandateDocAccess.getActive());
        mandateDocAccessView.setRoleId(mandateDocAccess.getRole().getId());
        mandateDocAccessView.setStepId(mandateDocAccess.getStep().getId());
        mandateDocAccessView.setCheckBRMS(mandateDocAccess.isCheckBRMS());
        mandateDocAccessView.setCheckECM(mandateDocAccess.isCheckECM());
        return mandateDocAccessView;
    }

    public Map<Long, MandateDocAccessView> transformToCache(List<MandateDocAccess> mandateDocAccessList){
        if(mandateDocAccessList == null || mandateDocAccessList.size() == 0)
            return null;
        Map<Long, MandateDocAccessView> _tmpMap = new ConcurrentHashMap<Long, MandateDocAccessView>();
        for(MandateDocAccess mandateDocAccess : mandateDocAccessList){
            MandateDocAccessView customerEntityView = transformToView(mandateDocAccess);
            _tmpMap.put(customerEntityView.getId(), customerEntityView);
        }
        return _tmpMap;

    }

}

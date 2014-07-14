package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.view.BaseRateView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BaseRateTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    private BaseRateDAO baseRateDAO;

    @Inject
    public BaseRateTransform() {
    }

    public BaseRate transformToModel(BaseRateView baseRateView){
        BaseRate baseRate = null;
        if(!Util.isNull(baseRateView) && !Util.isZero(baseRateView.getId())){
            baseRate = baseRateDAO.findById(baseRateView.getId());
        }
        return baseRate;
    }

    public BaseRateView transformToView(BaseRate baseRate){
        BaseRateView baseRateView = new BaseRateView();
        if(!Util.isNull(baseRate)){
            baseRateView.setId(baseRate.getId());
            baseRateView.setActive(baseRate.getActive());
            baseRateView.setName(baseRate.getName());
            baseRateView.setValue(baseRate.getValue());
        }
        return baseRateView;
    }
}

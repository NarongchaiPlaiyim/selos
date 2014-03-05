package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.working.BAPAInfoDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BaseRateView;
import com.clevel.selos.model.view.BasicInfoView;

import javax.inject.Inject;
import java.util.Date;

public class BaseRateTransform extends Transform {
    @Inject
    BaseRateDAO baseRateDAO;

    @Inject
    public BaseRateTransform() {
    }

    public BaseRate transformToModel(BaseRateView baseRateView){
        log.info("Start - transformToModel ::: baseRateView : {}", baseRateView);
        BaseRate baseRate = new BaseRate();

        if(baseRateView.getId() != 0){
            baseRate = baseRateDAO.findById(baseRateView.getId());
        }

        if(baseRateView!=null){
            baseRate.setName(baseRateView.getName());
            baseRate.setValue(baseRateView.getValue());
            baseRate.setActive(baseRateView.getActive());
        }

        log.info("End - transformToModel ::: baseRate : {}", baseRate);
        return baseRate;
    }

    public BaseRateView transformToView(BaseRate baseRate){
        log.info("Start - transformToView ::: baseRate : {}", baseRate);
        BaseRateView baseRateView = new BaseRateView();
        if(baseRate!=null){
            baseRateView.setId(baseRate.getId());
            baseRateView.setActive(baseRate.getActive());
            baseRateView.setName(baseRate.getName());
            baseRateView.setValue(baseRate.getValue());
        }

        log.info("End - transformToView ::: baseRateView : {}", baseRateView);
        return baseRateView;
    }
}

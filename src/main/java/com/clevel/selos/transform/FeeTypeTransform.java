package com.clevel.selos.transform;

import com.clevel.selos.dao.master.FeeTypeDAO;
import com.clevel.selos.model.db.master.FeeType;
import com.clevel.selos.model.view.FeeTypeView;

import javax.inject.Inject;

public class FeeTypeTransform extends Transform{

    @Inject
    FeeTypeDAO feeTypeDAO;

    @Inject
    public FeeTypeTransform(){}

    public FeeTypeView transformToView(FeeType feeType){
        FeeTypeView feeTypeView = new FeeTypeView();
        if(feeType != null && feeType.getId() != 0){
            feeTypeView.setId(feeType.getId());
            feeTypeView.setBrmsCode(feeType.getBrmsCode());
            feeTypeView.setDescription(feeType.getDescription());
            feeTypeView.setActive(feeType.getActive());
        }
        return feeTypeView;
    }

    public FeeType transformToModel(FeeTypeView feeTypeView){
        log.debug("begin transformToModel(feeTypeView {})", feeTypeView);
        if(feeTypeView != null && feeTypeView.getId() != 0) {
            try{
                FeeType feeType = feeTypeDAO.findById((int)feeTypeView.getId());
                return feeType;
            }catch (Exception ex){
                log.info("cannot find DisbursementType for transformToModel(feeTypeView {})", feeTypeView);
            }
        }
        return null;
    }
}

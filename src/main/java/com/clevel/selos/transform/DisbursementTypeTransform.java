package com.clevel.selos.transform;

import com.clevel.selos.dao.master.DisbursementTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.DisbursementType;
import com.clevel.selos.model.view.DisbursementTypeView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DisbursementTypeTransform extends Transform{
    @SELOS
    @Inject
    private Logger log;

    @Inject
    DisbursementTypeDAO disbursementTypeDAO;

    @Inject
    public DisbursementTypeTransform(){}

    public DisbursementTypeView transformToView(DisbursementType disbursementType){
        DisbursementTypeView disbursementTypeView = new DisbursementTypeView();
        if(disbursementType != null && disbursementType.getId() != 0){
            disbursementTypeView.setId(disbursementType.getId());
            disbursementTypeView.setDisbursement(disbursementType.getDisbursement());
            disbursementTypeView.setNo(disbursementType.getNo());
            disbursementTypeView.setActive(disbursementType.getActive());
        }
        return disbursementTypeView;
    }

    public DisbursementType transformToModel(DisbursementTypeView disbursementTypeView){
        log.debug("begin transformToModel(DisbursementTypeView {})", disbursementTypeView);
        if(disbursementTypeView != null && disbursementTypeView.getId() != 0) {
            try{
                DisbursementType disbursementType = disbursementTypeDAO.findById(disbursementTypeView.getId());
                return disbursementType;
            }catch (Exception ex){
                log.info("cannot find DisbursementType for transformToModel(DisbursementTypeView {})", disbursementTypeView);
            }
        }
        return null;
    }
}

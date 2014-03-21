package com.clevel.selos.transform;

import com.clevel.selos.dao.master.LoanPurposeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.LoanPurpose;
import com.clevel.selos.model.view.LoanPurposeView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class LoanPurposeTransform extends Transform{
    @SELOS
    @Inject
    private Logger log;

    @Inject
    LoanPurposeDAO loanPurposeDAO;

    @Inject
    public LoanPurposeTransform(){}

    public LoanPurposeView transformToView(LoanPurpose loanPurpose){
        LoanPurposeView loanPurposeView = new LoanPurposeView();
        if(loanPurpose != null && loanPurpose.getId() != 0){
            loanPurposeView.setId(loanPurpose.getId());
            loanPurposeView.setActive(loanPurpose.getActive());
            loanPurposeView.setBrmsCode(loanPurpose.getBrmsCode());
            loanPurposeView.setDescription(loanPurpose.getDescription());
        }
        return loanPurposeView;
    }

    public LoanPurpose transformToModel(LoanPurposeView loanPurposeView){
        log.debug("begin transformToModel(loanPurposeView {})", loanPurposeView);
        if(loanPurposeView != null && loanPurposeView.getId() != 0){
            try{
                LoanPurpose loanPurpose = loanPurposeDAO.findById(loanPurposeView.getId());
                return loanPurpose;
            } catch (Exception ex){
                log.info("cannot find LoanPurpose for {}", loanPurposeView);
            }
        }
        return null;
    }
}

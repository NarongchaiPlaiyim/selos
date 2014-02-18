package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.LoanPurposeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.LoanPurpose;
import com.clevel.selos.model.view.LoanPurposeView;
import com.clevel.selos.transform.LoanPurposeTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LoanPurposeControl extends BusinessControl {

    @Inject
    @SELOS
    Logger logger;

    @Inject
    LoanPurposeDAO loanPurposeDAO;

    @Inject
    LoanPurposeTransform loanPurposeTransform;

    @Inject
    public LoanPurposeControl(){}

    public List<LoanPurposeView> getLoanPurposeViewList(){

        List<LoanPurposeView> loanPurposeViewList = new ArrayList<LoanPurposeView>();
        List<LoanPurpose> loanPurposeList = loanPurposeDAO.findActiveAll();

        for(LoanPurpose loanPurpose : loanPurposeList){
            loanPurposeViewList.add(loanPurposeTransform.transformToView(loanPurpose));
        }
        return loanPurposeViewList;
    }
}

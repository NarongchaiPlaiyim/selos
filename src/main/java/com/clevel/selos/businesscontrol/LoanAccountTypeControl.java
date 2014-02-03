package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.view.LoanAccountTypeView;
import com.clevel.selos.transform.LoanAccountTypeTransform;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LoanAccountTypeControl extends BusinessControl {

    @Inject
    LoanAccountTypeTransform loanAccountTypeTransform;

    @Inject
    AccountTypeDAO loanAccountTypeDAO;

    @Inject
    BasicInfoDAO basicInfoDAO;

	@Inject
    public LoanAccountTypeControl(){

    }

    public List<LoanAccountTypeView> getListLoanTypeByWorkcase(long workCaseId) {
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        List<AccountType> loanAccountTypes = new ArrayList<AccountType>();
        if(basicInfo.getBorrowerType().getId() != 0){
           loanAccountTypes = loanAccountTypeDAO.getListLoanTypeByCusEntity(basicInfo.getBorrowerType().getId());
        }
        List<LoanAccountTypeView> loanTypeViews = loanAccountTypeTransform.getLoanAccountTypeViews(loanAccountTypes);
        return loanTypeViews;
    }

}

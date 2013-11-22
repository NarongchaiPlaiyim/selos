package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.LoanAccountTypeView;
import com.clevel.selos.transform.LoanAccountTypeTransform;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class LoanAccountTypeControl extends BusinessControl {

    @Inject
    LoanAccountTypeTransform loanAccountTypeTransform;

    @Inject
    AccountTypeDAO loanAccountTypeDAO;

    @Inject
    public LoanAccountTypeControl(){

    }

    public List<LoanAccountTypeView> getListLoanTypeByCus(int customerEntity) {
        List<AccountType> loanAccountTypes = loanAccountTypeDAO.getListLoanTypeByCusEntity(customerEntity);
        List<LoanAccountTypeView> loanTypeViews = loanAccountTypeTransform.getLoanAccountTypeViews(loanAccountTypes);
        return loanTypeViews;
    }

}
